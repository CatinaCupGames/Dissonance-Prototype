package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointSprite;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.RenderService;
import javafx.geometry.Pos;

import java.util.ArrayList;
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
                if (waypointList != null && waypointList.size() > 0) {
                    Position pos = waypointList.get(0);
                    if (pos.isShrunk())
                        pos.expand();
                    currentWaypoint = pos;
                    waypointList.remove(0);
                    _wakeup();
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

    public void setWaypoint(Position position, WaypointType type) {
        clearWaypoints();
        if (waypointList == null) {
            waypointList = new ArrayList<Position>();
        }
        if (type == WaypointType.SMART) {
            NodeMap map = getWorld().getNodeMap();
            waypointList = map.findPath(new Position(getX(), getY()).shrink(), position.shrink());
            if (waypointList.size() > 0)
                currentWaypoint = waypointList.get(0).expand();
        } else {
            currentWaypoint = position;
        }
    }

    public void addWaypoint(Position position, WaypointType type) {
        if (waypointList == null) {
            waypointList = new ArrayList<Position>();
        }
        if (type == WaypointType.SIMPLE) {
            waypointList.add(position);
            return;
        }
        if (type == WaypointType.SMART) {
            NodeMap map = getWorld().getNodeMap();
            List<Position> points = map.findPath(new Position(getX(), getY()).shrink(), position.shrink());
            if (points.size() > 0) {
                waypointList.addAll(points);
            }
        }
    }

    public void clearWaypoints() {
        if (waypointList != null)
            waypointList.clear();
        currentWaypoint = null;
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
        if (currentWaypoint == null || RenderService.INSTANCE == null)
            return;
        Position currentPos = currentWaypoint;
        while (true) {
            if (currentWaypoint == null || currentWaypoint != currentPos || RenderService.INSTANCE == null)
                break;
            super.wait(0L);
        }
    }

    public synchronized void waitForEndOfWaypointQueue() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        while (true) {
            if (currentWaypoint == null || RenderService.INSTANCE == null) { //If a new waypoint was not set, then there are no more waypoints.
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
