package com.dissonance.framework.game.ai.behaviors;

public interface Behavior {
    public void update();

    public void waypointStepped();

    public void waypointReached();
}
