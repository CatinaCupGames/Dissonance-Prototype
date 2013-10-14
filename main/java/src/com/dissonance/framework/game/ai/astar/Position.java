package com.dissonance.framework.game.ai.astar;

import org.jbox2d.common.Vec2;

public final class Position {

    private float x;
    private float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position(Vec2 vector) {
        this((int) vector.x, (int) vector.y);
    }

    public float getX() {
        return x;
    }

    protected void setX(float x) {
        this.x = x;
    }

    public Position expand() {
        return new Position(FastMath.fastFloor(getX() * 32), FastMath.fastFloor(getY() * 32));
    }

    public Position shrink() {
        return new Position(FastMath.fastFloor(getX() / 32), FastMath.fastFloor(getY() / 32));
    }

    public float getY() {
        return y;
    }

    protected void setY(float y) {
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
