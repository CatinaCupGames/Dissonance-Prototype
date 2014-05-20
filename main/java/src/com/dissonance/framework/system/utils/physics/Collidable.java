package com.dissonance.framework.system.utils.physics;

import com.dissonance.framework.game.world.World;

public interface Collidable {
    public float getX();

    public float getY();

    public HitBox getHitBox();

    public boolean isPointInside(float x, float y);

    public World getWorld();
}
