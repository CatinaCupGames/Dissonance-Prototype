package com.tog.framework.game.sprites;

import com.tog.framework.game.world.World;
import com.tog.framework.render.Texture;
import org.lwjgl.util.vector.Vector2f;

public interface Sprite {
    public Texture getTexture();

    public void setTexture(Texture texture);

    public void setWorld(World w);

    public World getWorld();

    public void setX(float x);

    public void setY(float y);

    public float getX();

    public float getY();

    public Vector2f getVector();
}
