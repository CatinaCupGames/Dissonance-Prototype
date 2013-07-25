package com.tog.framework.game.sprites;

import com.tog.framework.game.world.World;
import com.tog.framework.render.Drawable;
import com.tog.framework.render.Texture;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public abstract class Sprite implements Drawable {
    private Texture texture;
    private World world;
    private float x, y;
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

    public void setY(float y) {
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

    @Override
    public void render() {
        getTexture().bind();
        float cx = getTexture().getWidth();
        float cy = getTexture().getHeight();
        float bx = getTexture().getImageWidth();
        float by = getTexture().getImageHeight();
        final float x = getX(), y = getY();
        glColor3f(1f, 1f, 1f);
        glBegin(GL_QUADS);
        glTexCoord3f(x + cx, y, 0f);
        glVertex3f(x - bx, y + by, 0f);
        glTexCoord3f(x, y, 0f);
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord3f(x, y + cy, 0f);
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord3f(x + cx, y + cy, 0f);
        glVertex3f(x - bx, y - by, 0f);
        glEnd();
    }
}
