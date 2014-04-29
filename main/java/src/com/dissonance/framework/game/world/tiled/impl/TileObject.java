package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class TileObject extends Sprite {
    private static final HashMap<Integer, TexCordHolder> cache = new HashMap<Integer, TexCordHolder>();
    TexCordHolder tex_cords;

    TileSet parentTileSet;
    private int data_index;
    private final int ID;
    private boolean paralax_effect;
    private float parallax_speed = 0.5f;
    private Layer parentLayer;

    private int x;
    private int y;
    private int flipType;
    private float rotate;
    private boolean flippedHorizontally, flippedVertically, flippedDiagonally;

    public TileObject(int ID, TileSet parentTileSet, Layer parentLayer, int data_index) {
        this.ID = ID;
        this.parentLayer = parentLayer;
        this.parentTileSet = parentTileSet;
        this.data_index = data_index;

        x = data_index % parentLayer.getWidth();
        y = data_index / parentLayer.getWidth();

        x *= parentTileSet.getTileWidth();
        y *= parentTileSet.getTileHeight();
    }

    public void setFlippedHorizontally(boolean value) {
        this.flippedHorizontally = value;
        calculateFlip();
    }

    public void setFlippedVertically(boolean value) {
        this.flippedVertically = value;
        calculateFlip();
    }

    public void setFlippedDiagonally(boolean value) {
        this.flippedDiagonally = value;
        calculateFlip();
    }

    @Override
    public Texture getTexture() {
        return parentTileSet.getTexture();
    }

    public void init() {
        if (cache.containsKey(ID)) {
            tex_cords = cache.get(ID);
        } else if (parentTileSet.getTexture() != null) {
            tex_cords = new TexCordHolder();
            tex_cords.bottom_left = parentTileSet.getTexture().getTextureCord(Texture.BOTTOM_LEFT, ID, parentTileSet);
            tex_cords.bottom_right = parentTileSet.getTexture().getTextureCord(Texture.BOTTOM_RIGHT, ID, parentTileSet);
            tex_cords.top_left = parentTileSet.getTexture().getTextureCord(Texture.TOP_LEFT, ID, parentTileSet);
            tex_cords.top_right = parentTileSet.getTexture().getTextureCord(Texture.TOP_RIGHT, ID, parentTileSet);

            cache.put(ID, tex_cords);
        }


        if (!isGroundLayer()) {
            Layer high = getWorld().getHighestGroundLayer();
            Layer low = getWorld().getLowestGroundLayer();
            if (high.getLayerNumber() < getTiledLayer().getLayerNumber())
                setLayer(getTiledLayer().getLayerNumber() - high.getLayerNumber());
            else if (low.getLayerNumber() > getTiledLayer().getLayerNumber())
                setLayer(getTiledLayer().getLayerNumber() - low.getLayerNumber());
            else
                throw new InvalidParameterException("There is a non-ground layer in between 2 ground layers! (INVALID LAYER: " + getTiledLayer().getLayerNumber() + ")");
        } else
            setLayer(0);

        paralax_effect = getTiledLayer().isParallaxLayer();
        if (paralax_effect) {
            if (getTiledLayer().getProperty("parallax_speed") != null) {
                try {
                    parallax_speed = Float.parseFloat(getTiledLayer().getProperty("parallax_speed"));
                } catch (Throwable t) {
                    parallax_speed = 0.5f;
                }
            }
        }
    }

    private void calculateFlip() {
        flipType = 0;
        /*
        0 = none
        1 = horizontally
        2 = vertically
         */
        if (flippedHorizontally)
            flipType |= 1;  //Flip horizontally
        if (flippedVertically)
            flipType |= 2;  //Flip vertically

        if (flippedDiagonally) {
            if (flippedVertically && flippedHorizontally) {
                rotate = 90f;
                flipType ^= 2; //Don't flip vertically
            } else if (flippedHorizontally) {
                rotate = -90f;
                flipType ^= 2; //Don't flip vertically
            } else {
                rotate = 90f;
                flipType ^= 1; //Don't flip horizontally
            }
        } else
            rotate = 0f;
    }

    public Layer getTiledLayer() {
        return parentLayer;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public int getIndex() {
        return data_index;
    }

    @Override
    public void render() {
        if (parentTileSet.getTexture() == null)
            return;
        //if (isAlwaysAbove()) glDisable(GL_DEPTH_TEST);
        float alpha = parentLayer.getOpacity() - (1 - RenderService.getCurrentAlphaValue());
        if (alpha < 0)
            alpha = 0;
        glColor4f(1.0f, 1.0f, 1.0f, alpha);

        parentTileSet.getTexture().bind();
        if (paralax_effect) {
            float difx = (-Camera.getX() * parallax_speed) - -Camera.getX();
            float dify = (-Camera.getY() * parallax_speed) - -Camera.getY();
            glTranslatef(difx, dify, 0f);
            //this.y -= dify;
        }
        float bx = parentTileSet.getTileWidth() / 2;
        float by = parentTileSet.getTileHeight() / 2;
        float x = getX(), y = getY();
        float z = 0f;
        //float z = isGroundLayer() ? 99999 : y - by;

        glPushMatrix(); //Save the current view matrix
        glTranslatef(x, y, 0f); //Translate to the tile's position

        glRotatef(rotate, 0f, 0f, 1f); //Rotate 90 degrees on the z axis..because..you know..we're in 2d
        if ((flipType & 1) > 0) //Are we flipping horizontally?
            glScalef(-1f, 1f, 1f);
        if ((flipType & 2) > 0) //Are we flipping vertically?
            glScalef(1f, -1f, 1f);

        glBegin(GL_QUADS);
        glTexCoord2f(tex_cords.bottom_left.x, tex_cords.bottom_left.y); //bottom left
        glVertex3f(-bx, -by, z);
        glTexCoord2f(tex_cords.bottom_right.x, tex_cords.bottom_right.y); //bottom right
        glVertex3f(bx, -by, z);
        glTexCoord2f(tex_cords.top_right.x, tex_cords.top_right.y); //top right
        glVertex3f(bx, by, z);
        glTexCoord2f(tex_cords.top_left.x, tex_cords.top_left.y); //top left
        glVertex3f(-bx, by, z);
        glEnd();
        glPopMatrix(); //Reload the view matrix to original state
        parentTileSet.getTexture().unbind();

        glColor4f(1.0f, 1.0f, 1.0f, RenderService.getCurrentAlphaValue());
        if (paralax_effect) {
            float difx = -Camera.getX() - (-Camera.getX() * parallax_speed);
            float dify = -Camera.getY() - (-Camera.getY() * parallax_speed);
            glTranslatef(difx, dify, 0f);
        }
        //if (isAlwaysAbove()) glEnable(GL_DEPTH_TEST);
    }

    /*@Override
    public int compareTo(Drawable d) {
        if (isGroundLayer()) {
            if (d instanceof TileObject && ((TileObject)d).isGroundLayer()) {
                TileObject t = (TileObject)d;

                if (t.getTiledLayer().getLayerNumber() > getTiledLayer().getLayerNumber()) return UpdatableDrawable.BEFORE;
                else if (t.getTiledLayer().getLayerNumber() < getTiledLayer().getLayerNumber()) return UpdatableDrawable.AFTER;
                else return UpdatableDrawable.EQUAL;
            }
            return UpdatableDrawable.BEFORE;
        }
        else if (isAlwaysAbove())
            return UpdatableDrawable.AFTER;
        return super.compareTo(d);
    }*/

    public boolean isGroundLayer() {
        return getTiledLayer().isGroundLayer();
    }

    public boolean isParallaxLayer() {
        return paralax_effect;
    }

    public boolean isAlwaysAbove() {
        return parentTileSet.getTileProperty(ID, "above") != null && parentTileSet.getTileProperty(ID, "above").equalsIgnoreCase("true");
    }

    public boolean isAnimated() {
        return parentTileSet.getTileProperty(ID, "animated").equalsIgnoreCase("true");
    }

    public int[] getAnimatedTiles() {
        if (!isAnimated())
            return new int[0];
        String[] tiles = parentTileSet.getTileProperty(ID, "animated_ids").replaceAll(" ", "").split(",");

        int[] IDS = new int[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            try {
                IDS[i] = Integer.parseInt(tiles[i]);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return IDS;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.x;
        hash = 71 * hash + this.y;
        return hash;
    }

    private static class TexCordHolder {
        public Vector2f top_left;
        public Vector2f top_right;
        public Vector2f bottom_left;
        public Vector2f bottom_right;
    }
}
