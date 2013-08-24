package com.tog.framework.game.sprites;

import com.tog.framework.game.sprites.impl.PlayableSprite;
import com.tog.framework.game.world.World;
import com.tog.framework.render.Drawable;
import com.tog.framework.render.texture.Texture;
import org.lwjgl.util.vector.Vector2f;

import java.io.Serializable;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable, Serializable {
    protected transient Texture texture;
    protected transient World world;
    protected float x, y;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setWorld(World w) {
        this.world = w;
    }

    public World getWorld() {
        return world;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void onSelected(PlayableSprite player) {

    }

    public void setY(float y) {
        if (y != this.y && getWorld() != null)
            getWorld().invalidateDrawableList();
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2f getVector() {
        return new Vector2f(x, y);
    }

    public void onLoad() { }

    public void onUnload() { }

    @Override
    public void render() {
        getTexture().bind();
        float bx = getTexture().getTextureWidth() / 2;
        float by = getTexture().getTextureHeight() / 2;
        final float x = getX(), y = getY();
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
            Sprite s = (Sprite)o;
            float by = (getTexture() != null ? getTexture().getTextureHeight() / 4 : 0);
            float sy = (s.getTexture() != null ? s.getTexture().getTextureHeight() / 4 : 0);

            if (getY() - by < s.getY() - sy) return Drawable.BEFORE;
            else if (getY() - by > s.getY() - sy) return Drawable.AFTER;
            else return Drawable.EQUAL;
        } else
            return Drawable.AFTER;
    }
}
