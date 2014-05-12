package com.dissonance.framework.game.ai.astar;

public final class Vector {
    public float x;
    public float y;

    public Vector() {
        this(0, 0);
    }

    public Vector(Vector vector) {
        this(vector.x, vector.y);
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector add(float n) {
        return new Vector(x + n, y + n);
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector subtract(float n) {
        return new Vector(x - n, y - n);
    }

    public Vector multiply(Vector v) {
        return new Vector(x * v.x, y * v.y);
    }

    public Vector multiply(float n) {
        return new Vector(x * n, y * n);
    }

    public Vector divide(Vector v) {
        return new Vector(x / v.x, y / v.y);
    }

    public Vector divide(float n) {
        return new Vector(x / n, y / n);
    }

    public Vector invert() {
        return new Vector(-x, -y);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vector normalize() {
        if (x == 0 || y == 0) {
            return new Vector(0, 0);
        }

        return divide(length());
    }

    public Vector truncate(float max) {
        return length() > max ? normalize().multiply(max) : new Vector(this);
    }

    public Vector angle(float n) {
        float length = length();
        return new Vector((float) Math.cos(n) * length, (float) Math.sin(n) * length);
    }

    public boolean inRange(float minX, float maxX, float minY, float maxY) {
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    @Override
    public String toString() {
        return "[ vx: " + x + ", vy: " + y + " ]";
    }
}
