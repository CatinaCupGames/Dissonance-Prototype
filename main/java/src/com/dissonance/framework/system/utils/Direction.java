package com.dissonance.framework.system.utils;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT,
    NONE;

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
