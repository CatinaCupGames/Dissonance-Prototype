package com.dissonance.framework.system.utils;

public enum Direction {
    /**
     * Going towards y axis
     */
    UP,
    /**
     * Moving away from the y axis
     */
    DOWN,
    /**
     * Moving towards the x axis
     */
    LEFT,
    /**
     * Moving away from the x axis
     */
    RIGHT,
    /**
     * A complex direction <br></br>
     * Moving towards the y axis and towards the x axis
     */
    UP_LEFT,
    /**
     * A complex direction <br></br>
     * Moving towards the y axis and away from the x axis
     */
    UP_RIGHT,
    /**
     * A complex direction <br></br>
     * Moving away from the y axis and towards the x axis
     */
    DOWN_LEFT,
    /**
     * A complex direction <br></br>
     * Moving away from the y axis and away from the x axis
     */
    DOWN_RIGHT,
    /**
     * Represents no direction, or not moving.
     */
    NONE,

    /**
     * Represents any direction, or moving.
     */
    MOVING;

    /**
     * Converts a complex direction into a simple direction.
     * @return The only 2 results this can return is either {@link com.dissonance.framework.system.utils.Direction#UP} or {@link com.dissonance.framework.system.utils.Direction#DOWN} if this direction is a complex direction, otherwise it will return itself
     */
    public Direction simple() {
        switch (this) {
            case UP_RIGHT:
            case UP_LEFT:
                return UP;
            case DOWN_LEFT:
            case DOWN_RIGHT:
                return DOWN;
            default:
                return this;
        }
    }

    public Direction add(Direction dir) {
        if (this == Direction.UP && dir == Direction.LEFT)
            return Direction.UP_LEFT;
        else if (this == Direction.UP && dir == Direction.RIGHT)
            return Direction.UP_RIGHT;
        else if (this == Direction.DOWN && dir == Direction.LEFT)
            return Direction.DOWN_LEFT;
        else if (this == Direction.DOWN && dir == Direction.RIGHT)
            return Direction.DOWN_RIGHT;
        else if (this == Direction.LEFT && dir == Direction.UP)
            return Direction.UP_LEFT;
        else if (this == Direction.RIGHT && dir == Direction.UP)
            return Direction.UP_RIGHT;
        else if (this == Direction.LEFT && dir == Direction.DOWN)
            return Direction.DOWN_LEFT;
        else if (this == Direction.RIGHT && dir == Direction.DOWN)
            return Direction.DOWN_RIGHT;
        else
            return this;
    }

    /**
     * Get the opposite direction of this direction. For example, if this direction is {@link com.dissonance.framework.system.utils.Direction#RIGHT}, then this method will return {@link com.dissonance.framework.system.utils.Direction#LEFT}. <br></br>
     * If a complex direction is given, then a complex direction is returned. The complex direction returned will always be the opposite of what was given.
     * @return The opposite direction of this direction.
     */
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
            case UP_LEFT:
                return DOWN_RIGHT;
            case UP_RIGHT:
                return DOWN_LEFT;
            case DOWN_LEFT:
                return UP_RIGHT;
            case DOWN_RIGHT:
                return UP_LEFT;
            case NONE:
                return MOVING;
            case MOVING:
                return NONE;
            default:
                return this;
        }
    }
}
