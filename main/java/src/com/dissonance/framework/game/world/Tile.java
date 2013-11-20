package com.dissonance.framework.game.world;

import com.dissonance.framework.game.world.tiled.Layer;
import org.jbox2d.common.Vec2;

public class Tile {
    private final int id;
    private final int cost;
    private boolean passable = true;
    private final float x, y;
    private final Layer containingLayer;
    private final World parent;

    public Tile(int id, float x, float y, Layer layer, World world) {
        this.parent = world;
        this.id = id;

        int cost1;
        if (hasProperty("cost", world)) {
            try {
                cost1 = Integer.parseInt(getProperty("cost", world));
            } catch (Throwable t) {
                cost1 = 0;
            }
        } else {
            cost1 = 0;
        }
        cost = cost1;

        if (hasProperty("passable", world)) {
            passable = getProperty("passable", world).toLowerCase().equals("true");
        }

        this.x = x;
        this.y = y;
        this.containingLayer = layer;
    }

    public int getID() {
        return id;
    }

    public int getExtraCost() {
        return cost;
    }

    public boolean isPassable() {
        return passable;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Layer getContainingLayer() {
        return containingLayer;
    }

    public World getParentWorld() {
        return parent;
    }

    public Tile getRelativeTile(Vec2 addLocation) {
        return containingLayer.getTileAt(x + addLocation.x, y + addLocation.y, parent);
    }

    public Tile getTileAboveThis() {
        return getRelativeTile(new Vec2(1, 0));
    }

    public Tile getTileBelowThis() {
        return getRelativeTile(new Vec2(-1, 0));
    }

    public Tile getTileLeftOfThis() {
        return getRelativeTile(new Vec2(0, -1));
    }

    public Tile getTileRightOfThis() {
        return getRelativeTile(new Vec2(0, 1));
    }

    public String getProperty(String property, World world) {
        return world.getTiledData().findTileSetFromID(id).getTileProperty(this, property);
    }

    public boolean hasProperty(String property, World world) {
        return getProperty(property, world) != null;
    }
}
