package com.dissonance.framework.game.ai.astar;

import org.lwjgl.util.vector.Vector2f;

public final class Position {
    protected float x;
    protected float y;

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
        return new Position(FastMath.fastFloor(x * 32), FastMath.fastFloor(y * 32));
    }

    public Position shrink() {
        return new Position(FastMath.fastFloor(x / 32), FastMath.fastFloor(y / 32));
    }

    public float getY() {
        return y;
    }

    protected void setY(float y) {
        this.y = y;
    }

    public static Position add(Position a, Position b) {
        return new Position(a.x + b.x, a.y + b.y);
    }

    public static Position subtract(Position a, Position b) {
        return new Position(a.x - b.x, a.y - b.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }

        Position pos = (Position) obj;

        return (pos.x == x && pos.y == y);
    }

    @Override
    public String toString() {
        return "[ x: " + x + ", y: " + y + " ]";
    }
}
