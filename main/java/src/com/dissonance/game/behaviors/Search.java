package com.dissonance.game.behaviors;

import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.system.utils.Direction;

public class Search implements Behavior {
    private static final long SWITCH_TIME = 800L;

    private AbstractWaypointSprite owner;
    private Direction orginalDirection;
    private Direction face;
    private long startTime;
    public Search(AbstractWaypointSprite owner) {
        this.owner = owner;
        orginalDirection = owner.getFacingDirection();
        face = rotateDirection(orginalDirection);
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - startTime > SWITCH_TIME) {
            face = face.opposite();
            owner.face(face);
            startTime = System.currentTimeMillis();
        }
    }

    public Direction getOrginalDirection() {
        return orginalDirection;
    }

    private Direction rotateDirection(Direction direction) {
        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                return Direction.LEFT;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                return Direction.RIGHT;
            case LEFT:
                return Direction.UP;
            case RIGHT:
                return Direction.DOWN;
        }
        return Direction.NONE;
    }
}
