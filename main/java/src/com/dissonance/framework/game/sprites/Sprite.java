package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.util.vector.Vector2f;

import java.io.Serializable;
import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable, Serializable {
    private SpriteEvent.SpriteMovedEvent spriteMoved;

    protected transient Texture texture;
    protected transient World world;
    protected Direction direction;
    protected float x, y, width, height;

    public static Sprite fromClass(Class<?> class_) {
        if (!class_.isAssignableFrom(Sprite.class))
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

    public void setFacing(Direction direction) {
        this.direction = direction;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        width = texture.getTextureWidth();
        height = texture.getTextureHeight();
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
        this.x = x;
        if (spriteMoved != null)
            spriteMoved.onSpriteMoved(this, ox, y);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        float oy = this.y;
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

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        getTexture().unbind();
    }

    @Override
    public int compareTo(Drawable o) {
        if (o instanceof UIElement)
            return Drawable.BEFORE;
        else if (o instanceof Sprite) {
            Sprite s = (Sprite) o;
            if (s instanceof TileObject && ((TileObject)o).isGroundLayer())
                return Drawable.AFTER;
            else if (s instanceof TileObject && ((TileObject)o).isAlwaysAbove())
                return Drawable.BEFORE;
            float by = (getTexture() != null ? getTexture().getTextureHeight() / (this instanceof TileObject ? 2 : 4) : 0);
            float sy = (s.getTexture() != null ? s.getTexture().getTextureHeight() / (s instanceof TileObject ? 2 : 4) : 0);
            if (getY() - by < s.getY() - sy) return Drawable.BEFORE;
            else if (getY() - by > s.getY() - sy) return Drawable.AFTER;
            else return Drawable.EQUAL;
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
