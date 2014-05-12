package com.dissonance.framework.game.ai.astar;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

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

    public static Vector centerOf(List<Vector> points) {
        if (points.size() == 0)
            throw new InvalidParameterException("There must be at least 1 point to check against!");

        Vector center = new Vector(0, 0);
        float area = 0f;
        float x1, y1, x2, y2, a;

        for (int i = 0; i < points.size() - 1; i++) {
            Vector point1 = points.get(i);
            Vector point2 = points.get(i + 1);
            x1 = point1.x;
            y1 = point1.y;
            x2 = point2.x;
            y2 = point2.y;

            a = (x1 * y2) - (x2 * y1);
            area += a;
            center.x += (x1 + x2) * a;
            center.y += (y1 + y2) * a;
        }
        Vector point1 = points.get(points.size() - 1);
        Vector point2 = points.get(0);

        x1 = point1.x;
        y1 = point1.y;
        x2 = point2.x;
        y2 = point2.y;

        a = (x1 * y2) - (x2 * y1);
        area += a;
        center.x += (x1 + x2) * a;
        center.y += (y1 + y2) * a;

        area /= 2f;
        center.x /= (6f * area);
        center.y /= (6f * area);

        return center;
    }

    public static Vector centerOf(Vector... points) {
        return centerOf(Arrays.asList(points));
    }

    @Override
    public String toString() {
        return "[ vx: " + x + ", vy: " + y + " ]";
    }
}
