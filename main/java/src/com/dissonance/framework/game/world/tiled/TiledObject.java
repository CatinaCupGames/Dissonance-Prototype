package com.dissonance.framework.game.world.tiled;

import org.jbox2d.common.Vec2;

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

    public String getProperty(String key) {
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
            polygon = new Vec2[4];
            polygon[0] = new Vec2(x, y);
            polygon[1] = new Vec2(x + width, y);
            polygon[2] = new Vec2(x, y + height);
            polygon[3] = new Vec2(x + width, y + height);
            isSquare = true;

            return polygon;
        } else {
            return polygon;
        }
    }

    public boolean isSquare() {
        return !ellipse && (polygon == null || isSquare);
    }


    public boolean isBound() {
        return isBound;
    }

}
