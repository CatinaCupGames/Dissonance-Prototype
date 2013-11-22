package com.dissonance.framework.game.world.tiled;

import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class TiledObject {
    private boolean ellipse;
    private int height;
    private int width;
    private String name;
    private boolean visible;
    private int x;
    private int y;
    private HashMap<Object, Object> properties;
    private Vec2[] polygon;
    private boolean isSquare;
    private boolean isBound;
    private Polygon javaPolygon;

    public String getProperty(String key) {
        if (properties == null)
            return null;
        return (String) properties.get(key);
    }

    public boolean isVisible() {
        return visible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isEllipse() {
        return ellipse;
    }

    public Vec2[] getPolygonPoints() {
        if (ellipse) {
            return new Vec2[0];
        } if (polygon == null) {
            polygon = new Vec2[5];
            polygon[0] = new Vec2(0, 0);
            polygon[1] = new Vec2(width, 0);
            polygon[2] = new Vec2(width, height);
            polygon[3] = new Vec2(0, height);
            polygon[4] = polygon[0];
            isSquare = true;

            return polygon;
        } else {
            return polygon;
        }
    }

    public boolean isPointInside(float x, float y) {
        if (javaPolygon == null) {
            Vec2[] points = getPolygonPoints();
            if (points.length == 0)
                return false; //TODO Check for inside a circle
            javaPolygon = new Polygon();
            for (Vec2 point : points) {
                javaPolygon.addPoint((int)point.x, (int)point.y);
            }
            javaPolygon.translate(this.x, this.y);
        }

        return javaPolygon.contains(x, y);
    }



    public boolean isSquare() {
        return !ellipse && (polygon == null || isSquare);
    }


    public boolean isBound() {
        return isBound;
    }

}
