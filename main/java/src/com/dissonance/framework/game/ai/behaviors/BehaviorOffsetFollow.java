package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointMover;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.system.utils.Direction;

public class BehaviorOffsetFollow implements Behavior {

    private final AbstractWaypointSprite sprite; //The sprite that follows
    private final AbstractWaypointSprite target; //The sprite that's being followed
    private final Position offset; //The offset between the two sprites
    private long lastUpdate;
    private Direction direction;

    private static final float cos45 = (float) Math.cos(0.785398163);

    public BehaviorOffsetFollow(AbstractWaypointSprite sprite, AbstractWaypointSprite target, Position offset) {
        this.sprite = sprite;
        this.target = target;
        this.offset = offset;
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        direction = target.getDirection();

        if (now - lastUpdate > WaypointMover.SPEED * 2.5) {
            lastUpdate = now;
            Position destination;
            float x;
            float y;

            switch (direction) {
                case UP:
                case UP_LEFT:
                case LEFT:
                default:
                    x = target.getX() + offset.getX();
                    y = target.getY() + offset.getY();

                    destination = new Position(x, y);
                    break;
                case RIGHT:
                case UP_RIGHT:
                    x = verifyZero(target.getX() - offset.getY());
                    y = target.getY() + offset.getX();

                    destination = new Position(x, y);
                    break;
                case DOWN:
                case DOWN_RIGHT:
                    x = verifyZero(target.getX() - offset.getX());
                    y = verifyZero(target.getY() - offset.getY());

                    destination = new Position(x, y);
                    break;
                case DOWN_LEFT:
                    x = target.getX() + offset.getX();
                    y = verifyZero(target.getY() - offset.getY());

                    destination = new Position(x, y);
                    break;
            }

            sprite.setWaypoint(destination, WaypointType.SMART);
        }
    }

    private float verifyZero(float f) {
        if (f < 0) {
            return 0;
        }

        return f;
    }

    @Override
    public void waypointStepped() {
    }

    @Override
    public void waypointReached() {

    }
}
