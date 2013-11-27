package com.dissonance.framework.system.utils.physics;

public interface Collidable {
    public float getX();

    public float getY();

    public HitBox getHitBox();

    public boolean isPointInside(float x, float y);
}
