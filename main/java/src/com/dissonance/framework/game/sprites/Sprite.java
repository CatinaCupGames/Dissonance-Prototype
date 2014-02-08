package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.sprites.ui.impl.UIElement;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.Serializable;
import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable, Serializable {
    private SpriteEvent.SpriteMovedEvent spriteMoved;

    protected transient Texture texture;
    protected transient World world;
    protected Direction direction;
    protected float x, y, width, height;
    protected float r = 1, g = 1, b = 1, a = 1;
    protected boolean hasTint;
    protected int layer = 1;

    public static Sprite fromClass(Class<?> class_) {
        if (!Sprite.class.isAssignableFrom(class_))
            throw new InvalidParameterException("The class provided is not assignable to Sprite!");

        try {
            return (Sprite) class_.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("The class provided threw an InstantiationException!", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The class provided threw an IllegalAccessException!", e);
        }
    }

    public void setSpriteMovedListener(SpriteEvent.SpriteMovedEvent selectedListener) {
        this.spriteMoved = selectedListener;
    }

    public Texture getTexture() {
        return texture;
    }

    public Direction getDirection() {
        if (direction == null)
            direction = Direction.DOWN;
        return direction;
    }

    public void setTint(Color color) {
        r = color.getRed() / 255f;
        g = color.getGreen() / 255f;
        b = color.getBlue() / 255f;
        a = color.getAlpha() / 255f;
        hasTint = !(r == 1 && g == 1 && b == 1 && a == 1);
    }

    public void setTint(int r, int g, int b, int a) {
        Validator.validateInRange(r, 0, 255, "red");
        Validator.validateInRange(g, 0, 255, "green");
        Validator.validateInRange(b, 0, 255, "blue");
        Validator.validateInRange(a, 0, 255, "alpha");

        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
        this.a = a / 255f;
        hasTint = !(r == 1 && g == 1 && b == 1 && a == 1);
    }

    public void setTint(float r, float g, float b, float a) {
        Validator.validateInRange(r, 0, 1, "red");
        Validator.validateInRange(g, 0, 1, "green");
        Validator.validateInRange(b, 0, 1, "blue");
        Validator.validateInRange(a, 0, 1, "alpha");

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        hasTint = !(r == 1 && g == 1 && b == 1 && a == 1);
    }

    public Color getTint() {
        return new Color(r, g, b, a);
    }

    public boolean hasTint() {
        return hasTint;
    }

    public void removeTint() {
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
        hasTint = false;
    }

    public void setFacing(Direction direction) {
        this.direction = direction;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        width = texture.getTextureWidth();
        height = texture.getTextureHeight();
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
       return layer;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World w) {
        if (w == null) {
            this.world = null;
            return;
        }
        this.world = w;
    }

    public Tile getTileStandingOn() {
        if (world == null)
            return null;

        Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
        Layer lowest = null;
        for (Layer l : layers) {
            if (l == null)
                continue;
            if (isGroundLayer(l)) {
                lowest = l;
                break;
            }
        }

        if (lowest == null)
            return null;

        int x = (int)(getX() / 32);
        int y = (int)(getY() / 32);

        return lowest.getTileAt(x, y, world);
    }

    private boolean isGroundLayer(Layer l) {
        return (l.getProperty("ground") != null && l.getProperty("ground").equalsIgnoreCase("true"));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        float ox = this.x;

        if (x < 0) {
            x = 0;
        }

        this.x = x;
        if (spriteMoved != null)
            spriteMoved.onSpriteMoved(this, ox, y);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        float oy = this.y;

        if (y < 0) {
            y = 0;
        }
        if (y != this.y && getWorld() != null)
            getWorld().invalidateDrawableList();
        this.y = y;
        if (spriteMoved != null)
            spriteMoved.onSpriteMoved(this, x, oy);
    }

    public void setPos(Vector2f vector2f) {
        setX(vector2f.x);
        setY(vector2f.y);
    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float w) {
        width = w;
    }

    public void setHeight(float h) {
        height = h;
    }

    public Vector2f getVector() {
        return new Vector2f(x, y);
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    public void onLoad() {
    }

    public void onUnload() {
        texture = null;
    }

    @Override
    public void render() {
        getTexture().bind();
        float bx = width / 2;
        float by = height / 2;
        final float x = getX(), y = getY();
        float z = 0f;
        //float z = -(y - (by / 2));

        if (hasTint) {
            float alpha = RenderService.getCurrentAlphaValue();
            if (a < 1) {
                alpha = this.a - (1 - RenderService.getCurrentAlphaValue());
                if (alpha < 0)
                    alpha = 0;
            }
            glColor4f(r, g, b, alpha);
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        getTexture().unbind();
        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
    }

    @Override
    public int compareTo(Drawable o) {
        if (o instanceof UI)
            return Drawable.BEFORE;
        else if (o instanceof Sprite) {
            Sprite s = (Sprite)o;
            if (s.getLayer() > getLayer()) return Drawable.BEFORE;
            else if (s.getLayer() < getLayer()) return Drawable.AFTER;
            else {
                //float by = (getTexture() != null ? getTexture().getTextureHeight() / (this instanceof TileObject ? 2 : 4) : 0);
                //float sy = (s.getTexture() != null ? s.getTexture().getTextureHeight() / (s instanceof TileObject ? 2 : 4) : 0);
                float by = getHeight();
                float sy = s.getHeight();
                return (int)((getY() - by) - (s.getY() - sy));
            }
        }
        return Drawable.AFTER;
    }

    public interface SpriteEvent {

        public interface SpriteMovedEvent {
            public void onSpriteMoved(Sprite sprite, float oldx, float oldy);
        }
        //TODO Add more listeners
    }
}
