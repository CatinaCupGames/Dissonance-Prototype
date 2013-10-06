package com.dissonance.framework.game.ai.astar;

import org.jbox2d.common.Vec2;

public final class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Vec2 vector) {
        this((int) vector.x, (int) vector.y);
    }

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }

        Position pos = (Position) obj;

        return (pos.x == x && pos.y == y);
    }
}
