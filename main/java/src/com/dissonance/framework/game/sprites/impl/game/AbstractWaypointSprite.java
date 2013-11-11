package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointSprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.RenderService;

import java.util.List;

public abstract class AbstractWaypointSprite extends AnimatedSprite implements WaypointSprite {
    private WaypointSpriteEvent.OnWaypointReachedEvent waypointReachedEvent;

    protected Position currentWaypoint;
    protected List<Position> waypointList;

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
        super.update();
        if (isUpdateCanceled())
            return;
        if (currentWaypoint != null && waypointList != null) {

            if (!WaypointMover.moveSpriteOneFrame(this)) {
                if (waypointList.size() > 0) {
                    currentWaypoint = waypointList.get(0).expand();
                    waypointList.remove(0);
                } else {
                    currentWaypoint = null;
                    _wakeup();
                    if (waypointReachedEvent != null) {
                        waypointReachedEvent.onWaypointReached(this);
                    }
                }
            }
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        waypointList.clear();
        waypointList = null;
    }

    public void setWaypoint(Position position) {

        NodeMap map = getWorld().getNodeMap();
        waypointList = map.findPath(new Position(getX(), getY()).shrink(), position.shrink());
        if (waypointList.size() > 0)
            currentWaypoint = waypointList.get(0).expand();
    }

    public void appendWaypoint(Position position) {
        if (waypointList == null) {
            setWaypoint(position);
            return;
        }
        NodeMap map = getWorld().getNodeMap();
        List<Position> points = map.findPath(new Position(getX(), getY()).shrink(), position.shrink());
        if (points.size() > 0) {
            waypointList.addAll(points);
        }
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
