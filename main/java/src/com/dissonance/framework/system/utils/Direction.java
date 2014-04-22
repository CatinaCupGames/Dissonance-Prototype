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
    NONE;

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
}
