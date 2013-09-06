package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.ai.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointSprite;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.render.RenderService;

public abstract class AbstractWaypointSprite extends AnimatedSprite implements WaypointSprite {
    protected Position currentWaypoint;

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
        while (true) {
            if (currentWaypoint == null || RenderService.INSTANCE == null)
                break;
            super.wait(0L);
        }
    }
}
