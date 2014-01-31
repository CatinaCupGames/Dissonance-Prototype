package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

public interface Behavior {
    public void update();

    public void waypointStepped();

    public void waypointReached();

    public AbstractWaypointSprite ignoreSprite();
}
