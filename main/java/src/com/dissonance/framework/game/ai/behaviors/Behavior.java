package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

public interface Behavior {
    static final float MAX_VELOCITY = 1;
    static final float MAX_FORCE = 0.133333333333334f;

    /**
     * This method is called whenever the parent sprite's {@link AbstractWaypointSprite#update()}
     * method is called.
     */
    public void update();
}
