package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.HashMap;

public class TiledObject implements Collidable {
    private boolean ellipse;
    private float height;
    private float width;
    private String name;
    private boolean visible;
    private float x;
    private float y;
    private String type;
    private HashMap<Object, Object> properties;
    private Vector2f[] polygon;
    private boolean isSquare;
    private boolean isBound;
    private Polygon javaPolygon;
    private HitBox hitBox;
    World world;

    private AbstractTrigger trigger;

    private TiledObject() {
    }

    public String getProperty(String key) {
        if (properties == null)
            return null;
        return (String) properties.get(key);
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public HitBox getHitBox() {
        if (hitBox == null) {
            hitBox = new HitBox(0, 0, width, height);
        }
        return hitBox;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isEllipse() {
        return ellipse;
    }

    public Vector2f[] getPolygonPoints() {
        if (ellipse) {
            return new Vector2f[0];
        }
        if (polygon == null) {
            polygon = new Vector2f[5];
            polygon[0] = new Vector2f(x - 9, y - 9);
            polygon[1] = new Vector2f(x + (width - 6), y - 9);
            polygon[2] = new Vector2f(x + (width - 6), y + (height - 6));
            polygon[3] = new Vector2f(x - 9, y + (height - 6));
            polygon[4] = polygon[0];
            isSquare = true;

            return polygon;
        } else {
            return polygon;
        }
    }

    public boolean isHitbox() {
        return type == null || type.equals("") || type.toLowerCase().equals("hitbox");
    }

    public boolean isDoor() {
        return type != null && type.toLowerCase().equals("door");
    }

    public boolean isSpawn() {
        return type != null && type.toLowerCase().equals("spawn");
    }

    public boolean isTrigger() {
        return type != null && type.toLowerCase().equals("trigger");
    }

    public AbstractTrigger getTrigger() {
        return trigger;
    }

    void attachTrigger(AbstractTrigger trigger) {
        this.trigger = trigger;
    }

    public String getDoorTarget() {
        String value = getProperty("spawn");
        if (value != null)
            return value;
        return "";
    }

    public String getDoorWorldTarget() {
        String value = getProperty("world");
        if (value != null)
            return value;
        return "";
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return isPointInside(new Vector2f(x, y));
    }

    @Override
    public World getWorld() {
        return null;
    }

    public boolean isPointInside(Vector2f test) {
        int i;
        int j;
        boolean result = false;
        Vector2f[] points = getPolygonPoints();
        for (i = 0, j = points.length - 1; i < points.length; j = i++) {
            if ((points[i].y > test.y) != (points[j].y > test.y) &&
                    (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)) {
                result = !result;
            }
        }
        return result;
    }


    public boolean isSquare() {
        return !ellipse && (polygon == null || isSquare);
    }


    public boolean isBound() {
        return isBound;
    }

    public String getRawType() {
        return type;
    }
}
