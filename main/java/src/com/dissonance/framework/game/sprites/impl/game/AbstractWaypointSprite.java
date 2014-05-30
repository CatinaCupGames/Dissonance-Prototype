package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointSprite;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWaypointSprite extends AnimatedSprite implements WaypointSprite {
    private WaypointSpriteEvent.OnWaypointReachedEvent waypointReachedEvent;
    protected WaypointMover waypointMover = WaypointMover.Simple;
    protected Position currentWaypoint;
    protected List<Position> waypointList;
    protected float movementSpeed = 10f;
    private Behavior behavior;
    private Position startPos = null;
    private Vector steeringVelocity = new Vector(0, 0);

    /**
     * Sets this {@link WaypointSprite WaypointSprite's}
     * {@link WaypointSpriteEvent.OnWaypointReachedEvent OnTalkEvent listener} to the specified listener.
     *
     * @param waypointReachedListener The new event listener.
     */
    public void setWaypointReachedListener(WaypointSpriteEvent.OnWaypointReachedEvent waypointReachedListener) {
        this.waypointReachedEvent = waypointReachedListener;
    }

    public void setWaypointMover(WaypointMover mover) {
        this.waypointMover = mover;
    }

    public WaypointMover getWaypointMover() {
        return waypointMover;
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;

        if (currentWaypoint != null && waypointList != null) {
            if (startPos == null)
                startPos = getPosition();
            if (!waypointMover.moveSpriteOneFrame(this, startPos)) {
                if (waypointList != null && waypointList.size() > 0) {
                    startPos = null;
                    currentWaypoint = waypointList.get(0).expand(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight());
                    waypointList.remove(0);
                    _wakeup();
                } else {
                    startPos = null;
                    currentWaypoint = null;
                    _wakeup();
                    if (waypointReachedEvent != null) {
                        waypointReachedEvent.onWaypointReached(this);
                    }
                }
            }
        } else if (behavior != null) {
            behavior.update();
        }
    }

    /*@Override
    public void render() {
        super.render();

        if (behavior != null && (behavior instanceof LeaderFollow || behavior instanceof PathFollow)) {
            glLineWidth(3);
            glColor3f(255, 0, 0);
            glBegin(GL_LINE_STRIP);
            glVertex2f(x, y);
            for (Position p : (behavior instanceof LeaderFollow ? ((LeaderFollow) behavior).getNodes() : ((PathFollow) behavior).getNodes())) {
                glVertex2f(p.getX() * 16, p.getY() * 16);
            }
            glEnd();
            glColor3f(0, 255, 0);
            glBegin(GL_LINE_STRIP);
            glVertex2f(x, y);
            glVertex2f(x + steeringVelocity.normalize().x * 25f, y + steeringVelocity.normalize().y * 25f);
            glEnd();
            glColor3f(255, 255, 255);
            glLineWidth(1);
        }
    }*/

    @Override
    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float value) {
        this.movementSpeed = value;
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
            waypointList = new ArrayList<>();
        }
        if (type == WaypointType.SMART) {
            NodeMap map = getWorld().getNodeMap();
            waypointList = map.findPath(new Position(getX(), getY()).shrink(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight()), position.shrink(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight()));
            if (waypointList.size() > 1) {
                currentWaypoint = waypointList.get(1).expand(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight());
                waypointList.remove(0);
            } else if (waypointList.size() > 0) {
                currentWaypoint = waypointList.get(0).expand(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight());
            }
        } else {
            currentWaypoint = position;
        }
    }

    public void setWaypoint(float x, float y, WaypointType type){
        setWaypoint(new Position(x, y), type);
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
            List<Position> points = map.findPath(new Position(getX(), getY()).shrink(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight()), position.shrink(getWorld().getTiledData().getTileWidth(), getWorld().getTiledData().getTileHeight()));
            if (points.size() > 0) {
                waypointList.addAll(points);
            }
        }
    }

    public void addWaypoint(float x, float y, WaypointType type){
        addWaypoint(new Position(x, y), type);
    }

    public void clearWaypoints() {
        if (waypointList != null)
            waypointList.clear();
        currentWaypoint = null;
    }

    public final List<Position> getWaypointList() {
        return waypointList;
    }
    @Override
    public Position getWaypoint() {
        return currentWaypoint;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public final void setSteeringVelocity(Vector steeringVelocity) {
        this.steeringVelocity = steeringVelocity;
    }

    public final Vector getSteeringVelocity() {
        return steeringVelocity;
    }

    public final Vector getPositionVector() {
        return new Vector(x, y);
    }

    public final Vector getFeetVector() {
        return new Vector(x, y + height / 2);
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

    public void moveTowards(Direction direction, float distance) {
        float xadd, yadd;
        switch (direction) {

            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case UP_LEFT:
                break;
            case UP_RIGHT:
                break;
            case DOWN_LEFT:
                break;
            case DOWN_RIGHT:
                break;
            case NONE:
                break;
            case MOVING:
                break;
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
