package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.ai.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointSprite;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.render.RenderService;

public abstract class AbstractWaypointSprite extends AnimatedSprite implements WaypointSprite {
    private WaypointSpriteEvent.OnWaypointReachedEvent waypointReachedEvent;

    protected Position currentWaypoint;

    /**
     * Sets this {@link WaypointSprite WaypointSprite's}
     * {@link WaypointSpriteEvent.OnWaypointReachedEvent OnTalkEvent listener} to the specified listener.
     *
     * @param waypointReachedListener The new event listener.
     */
    public void setWaypointReachedListener(WaypointSpriteEvent.OnWaypointReachedEvent waypointReachedListener) {
        this.waypointReachedEvent = waypointReachedListener;
    }

    @Override
    public void update() {
        if (currentWaypoint != null && !WaypointMover.moveSpriteOneFrame(this)) {
            currentWaypoint = null;
            _wakeup();
        }
    }

    public void setWaypoint(Position position) {
        this.currentWaypoint = position;
    }

    @Override
    public Position getWaypoint() {
        return currentWaypoint;
    }

    private synchronized void _wakeup() {
        super.notifyAll();
    }

    public synchronized void waitForWaypointReached() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        while (true) {
            if (currentWaypoint == null || RenderService.INSTANCE == null) {
                break;
            }
            super.wait(0L);
        }

        if (waypointReachedEvent != null) {
            waypointReachedEvent.onWaypointReached(this);
        }
    }

    public interface WaypointSpriteEvent {
        /**
         * Interface definition for a callback to be invoked when the {@link WaypointSprite} has reached its destination.
         */
        public interface OnWaypointReachedEvent {
            public void onWaypointReached(WaypointSprite sprite);
        }
    }
}
