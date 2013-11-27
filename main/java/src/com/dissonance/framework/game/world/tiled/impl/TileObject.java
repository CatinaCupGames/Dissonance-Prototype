package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.texture.Texture;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class TileObject extends Sprite {
    private static final HashMap<Integer, TexCordHolder> cache = new HashMap<Integer, TexCordHolder>();
    private TexCordHolder tex_cords;

    private TileSet parentTileSet;
    private int data_index;
    private final int ID;
    private Layer parentLayer;

    private int x;
    private int y;

    public TileObject(int ID, TileSet parentTileSet, Layer parentLayer, int data_index) {
        this.ID = ID;
        this.parentLayer = parentLayer;
        this.parentTileSet = parentTileSet;
        this.data_index = data_index;

        x = data_index % parentLayer.getWidth();
        y = data_index / parentLayer.getHeight();

        x *= parentTileSet.getTileWidth();
        y *= parentTileSet.getTileHeight();
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
    }

    public Layer getLayer() {
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
        glColor4f(1.0f, 1.0f, 1.0f, parentLayer.getOpacity());

        parentTileSet.getTexture().bind();
        float bx = parentTileSet.getTileWidth() / 2;
        float by = parentTileSet.getTileHeight() / 2;
        float x = getX(), y = getY();

        glBegin(GL_QUADS);
        glTexCoord2f(tex_cords.bottom_left.x, tex_cords.bottom_left.y); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(tex_cords.bottom_right.x, tex_cords.bottom_right.y); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(tex_cords.top_right.x, tex_cords.top_right.y); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(tex_cords.top_left.x, tex_cords.top_left.y); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        parentTileSet.getTexture().unbind();

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public int compareTo(Drawable d) {
        if (isGroundLayer()) {
            if (d instanceof TileObject && ((TileObject)d).isGroundLayer()) {
                TileObject t = (TileObject)d;

                if (t.getLayer().getLayerNumber() > getLayer().getLayerNumber()) return UpdatableDrawable.BEFORE;
                else if (t.getLayer().getLayerNumber() < getLayer().getLayerNumber()) return UpdatableDrawable.AFTER;
                else return UpdatableDrawable.EQUAL;
            }
            return UpdatableDrawable.BEFORE;
        }
        else if (isAlwaysAbove())
            return UpdatableDrawable.AFTER;
        return super.compareTo(d);
    }

    public boolean isGroundLayer() {
        return (getLayer().getProperty("ground") != null && getLayer().getProperty("ground").equalsIgnoreCase("true"));
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

    private static class TexCordHolder {
        public Vector2f top_left;
        public Vector2f top_right;
        public Vector2f bottom_left;
        public Vector2f bottom_right;
    }
}
