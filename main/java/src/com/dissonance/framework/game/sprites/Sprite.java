package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.impl.EdgeGlowShader;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.Serializable;
import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable, Serializable {
    private static EdgeGlowShader EDGE_SHADER;
    private SpriteEvent.SpriteMovedEvent spriteMoved;

    protected transient Texture texture;
    protected boolean visible = true;
    protected transient World world;
    protected Direction direction;
    protected float x, y, width, height;
    protected float r = 1, g = 1, b = 1, a = 1;
    protected boolean hasTint;
    protected int layer = 1;
    protected boolean isTeleporting;
    protected boolean glowing;

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

    public boolean isTeleporting() {
        return isTeleporting;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Direction getFacingDirection() {
        if (direction == null)
            direction = Direction.DOWN;
        return direction;
    }

    public void glow() {
        glowing = true;
    }

    public void removeGlow() {
        glowing = false;
    }

    public float getAlpha() {
        return a;
    }

    public void setAlpha(float a) {
        this.a = a;
        hasTint = a != 1f;
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

    public void setFacingDirection(Direction direction) {
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

        int x = (int) (getX() / 16f);
        int y = (int) (getY() / 16f);

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

    private boolean loaded = false;
    public void onLoad() {
    }

    public final void completeLoading() {
        if (loaded)
            throw new IllegalAccessError("The sprite \"" + toString() + "\" has already been loaded!");
        loaded = true;
        _wakeLoaders();
    }

    public synchronized void waitForLoaded() throws InterruptedException {
        while (true) {
            if (loaded)
                break;
            super.wait(0L);
        }
    }

    private synchronized void _wakeLoaders() {
        super.notifyAll();
    }

    public void onUnload() {
        texture = null;
        loaded = false;
    }

    @Override
    public boolean neverClip() {
        return false;
    }

    @Override
    public boolean neverSort() {
        return false;
    }

    @Override
    public void render() {
        if (!visible)
            return;
        if (glowing) {
            renderGlow();
        }
        getTexture().bind();
        float bx = width / 2f;
        float by = height / 2f;
        final float x = getX(), y = getY();
        float z = 0f;
        //float z = -(y - (by / 2));

        if (hasTint) {
            float alpha = RenderService.getCurrentAlphaValue();
            if (a < 1) {
                alpha = this.a - (1f - RenderService.getCurrentAlphaValue());
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

    protected boolean glowPass = false;
    protected void renderGlow() {
        if (glowPass)
            return;
        if (EDGE_SHADER == null) {
            EDGE_SHADER = new EdgeGlowShader();
            EDGE_SHADER.build();
        }
        EDGE_SHADER.preRender();

        float oWidth = getWidth();
        float oHeight = getHeight();

        setWidth(oWidth + 4);
        setHeight(oHeight + 4);

        glowPass = true;
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        render();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glowPass = false;

        setWidth(oWidth);
        setHeight(oHeight);

        EDGE_SHADER.postRender();
    }

    @Override
    public int compareTo(Drawable o) {
        if (o instanceof UI)
            return Drawable.BEFORE;
        else if (o instanceof Sprite) {
            Sprite s = (Sprite) o;
            if (s.getLayer() > getLayer()) return Drawable.BEFORE;
            else if (s.getLayer() < getLayer()) return Drawable.AFTER;
            else {
                //float by = (getTexture() != null ? getTexture().getTextureHeight() / (this instanceof TileObject ? 2 : 4) : 0);
                //float sy = (s.getTexture() != null ? s.getTexture().getTextureHeight() / (s instanceof TileObject ? 2 : 4) : 0);
                float by = getHeight() / 2f;
                float sy = s.getHeight() / 2f;
                return (int) ((getY() - by) - (s.getY() - sy));
            }
        }
        return Drawable.AFTER;
    }

    /**
     * Get the distance from another sprite.
     * @param target The target sprite calculate distance to.
     * @return The distance, in pixels, to the sprite <b>target</b>
     */
    public double distanceFrom(Sprite target) {
        return Math.sqrt(((getX() - target.getX()) * (getX() - target.getX())) + ((getY() - target.getY()) * (getY() - target.getY())));
    }

    /**
     * Get the direction towards the sprite in the parameter.
     * @param sprite The sprite to get the direction towards
     * @return The direction towards this sprite. This method will always return a {@link com.dissonance.framework.system.utils.Direction#simple()}  direction.
     * @see com.dissonance.framework.system.utils.Direction#simple()
     */
    public Direction directionTowards(Sprite sprite) {
        double angle = angleTowards(sprite);
        if ((angle > 315 || angle < 45)) {
            return Direction.RIGHT;
        } else if (angle > 255 && angle <= 315) {
            return Direction.DOWN;
        } else if (angle > 135 && angle <= 225) {
            return Direction.LEFT;
        } else if (angle >= 45 && angle <= 135) {
            return Direction.UP;
        }
        return Direction.NONE;
    }

    /**
     * This method returns the angler direction of the sprite in the parameter. For example, if this sprite is above the parameter sprite, this method should return 270 degrees.
     * @param sprite The sprite to check against
     * @return The angler direction of the selected sprite.
     */
    public double angleTowards(Sprite sprite) {
        float ydif = sprite.getY() - getY();
        float xdif = sprite.getX() - getX();
        double angle = Math.toDegrees(Math.atan2(-ydif, xdif));
        while (angle < 0)
            angle += 360;
        while (angle > 360)
            angle -= 360;
        return angle;
    }

    /**
     * Get the direction towards the vector in the parameter.
     * @param target The vector to get the direction towards
     * @return The direction towards the vector specified. This method will always return a {@link com.dissonance.framework.system.utils.Direction#simple()}  direction.
     * @see com.dissonance.framework.system.utils.Direction#simple()
     */
    public Direction directionTowards(Vector target) {
        double angle = angleTowards(target);
        if ((angle > 315 || angle < 45)) {
            return Direction.RIGHT;
        } else if (angle > 255 && angle <= 315) {
            return Direction.DOWN;
        } else if (angle > 135 && angle <= 225) {
            return Direction.LEFT;
        } else if (angle >= 45 && angle <= 135) {
            return Direction.UP;
        }
        return Direction.NONE;
    }

    /**
     * This method returns the angler direction of the vector in the parameter. For example, if this sprite is above the vector, this method should return 270 degrees.
     * @param target The sprite to check against
     * @return The angler direction of the vector.
     */
    private double angleTowards(Vector target) {
        float ydif = target.y - getY();
        float xdif = target.x - getX();
        double angle = Math.toDegrees(Math.atan2(-ydif, xdif));
        while (angle < 0)
            angle += 360;
        while (angle > 360)
            angle -= 360;
        return angle;
    }

    public double distanceFrom(Vector target) {
        return Math.sqrt(((getX() - target.x) * (getX() - target.x)) + ((getY() - target.y) * (getY() - target.y)));
    }

    public interface SpriteEvent {

        public interface SpriteMovedEvent {
            public void onSpriteMoved(Sprite sprite, float oldx, float oldy);
        }
        //TODO Add more listeners
    }
}
