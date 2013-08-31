package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.utils.Direction;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import java.io.Serializable;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable, Serializable {
    protected transient Body physicsBody;
    protected transient BodyDef physicsBodyDef;
    protected transient Texture texture;
    protected transient World world;
    protected Direction facing_direction;
    protected float x, y;

    public Texture getTexture() {
        return texture;
    }

    public Direction getFacingDirection() {
        return facing_direction;
    }

    public void setFacing(Direction direction) {
        this.facing_direction = direction;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World w) {

        if (this.physicsBody != null)
            // Dealloc reference to body //
            w.getPhysicsWorld().destroyBody(this.physicsBody);

        this.world = w;
        this.physicsBodyDef = new BodyDef();
        this.physicsBodyDef.active = false;
        this.physicsBodyDef.awake = false;
        this.physicsBodyDef.type = BodyType.DYNAMIC;
        this.physicsBody = w.getPhysicsWorld().createBody(physicsBodyDef);
    }

    public void onSelected(PlayableSprite player) {

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y != this.y && getWorld() != null)
            getWorld().invalidateDrawableList();
        this.y = y;
    }

    public Vec2 getVector() {
        return new Vec2(x, y);
    }

    public void onLoad() {
    }

    public void onUnload() {
    }

    @Override
    public void render() {
        getTexture().bind();
        float bx = getTexture().getTextureWidth() / 2;
        float by = getTexture().getTextureHeight() / 2;
        final float x = getX(), y = getY();

        if(physicsBodyDef.active) {
            //TODO: Camera.worldToScreen
        }

        //glColor3f(1f, .5f, .5f); DEBUG LINE FOR TEXTURES
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
        if (o instanceof Sprite) {
            Sprite s = (Sprite) o;
            float by = (getTexture() != null ? getTexture().getTextureHeight() / 4 : 0);
            float sy = (s.getTexture() != null ? s.getTexture().getTextureHeight() / 4 : 0);

            if (getY() - by < s.getY() - sy) return Drawable.BEFORE;
            else if (getY() - by > s.getY() - sy) return Drawable.AFTER;
            else return Drawable.EQUAL;
        } else
            return Drawable.AFTER;
    }
}
