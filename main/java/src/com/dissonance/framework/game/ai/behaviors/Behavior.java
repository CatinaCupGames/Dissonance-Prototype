package com.dissonance.framework.game.ai.behaviors;

public interface Behavior {
    public static final float MAX_VELOCITY = 1;
    public static final float MAX_FORCE = 0.133333333333334f;

    public void update();
}
