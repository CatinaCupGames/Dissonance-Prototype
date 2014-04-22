package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

public interface Behavior {
    /**
     * Similar to the {@link com.dissonance.framework.render.UpdatableDrawable#update()} method, this method is in the render
     * thread every frame.
     */
    public void update();

    /**
     * This method is called whenever the parent sprite completed a single waypoint.
     */
    public void waypointStepped();

    /**
     * This method is called whenever the parent sprite has completed all waypoints in its waypoint queue.
     */
    public void waypointReached();

    /**
     * This method should return a {@link com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite} to ignore
     * when checking for collision, or none if no sprite should be ignored
     * @return The {@link com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite} to ignore when checking for collision.
     */
    public AbstractWaypointSprite ignoreSprite();
}
