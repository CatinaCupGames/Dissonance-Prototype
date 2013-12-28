package com.dissonance.framework.game.ai.astar;

import org.lwjgl.util.vector.Vector2f;

public final class Position {

    protected float x;
    protected float y;
    private boolean shrunk;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Position(Vector2f vector) {
        this((int) vector.x, (int) vector.y);
    }

    public float getX() {
        return x;
    }

    protected void setX(float x) {
        this.x = x;
    }

    public Position expand() {
        shrunk = false;
        return new Position(FastMath.fastFloor(x * 32), FastMath.fastFloor(y * 32));
    }

    public Position shrink() {
        shrunk = true;
        return new Position(FastMath.fastFloor(x / 32), FastMath.fastFloor(y / 32));
    }

    public boolean isShrunk() {
        return shrunk;
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
