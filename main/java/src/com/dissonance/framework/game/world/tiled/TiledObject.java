package com.dissonance.framework.game.world.tiled;

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
    private TiledPoint[] polygon;

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

    public TiledPoint[] getPolygonPoints() {
        if (ellipse || polygon == null) {
            return new TiledPoint[0];
        } else {
            return polygon;
        }
    }

    public boolean isSquare() {
        return !ellipse && polygon == null;
    }


}
