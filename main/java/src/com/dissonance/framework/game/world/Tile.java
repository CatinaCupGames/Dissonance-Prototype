package com.dissonance.framework.game.world;

import com.dissonance.framework.game.world.tiled.Layer;
import org.jbox2d.common.Vec2;

public class Tile {
    private TileType type;
    private final float x, y;
    private final Layer containingLayer;

    public Tile(TileType type, float x, float y, Layer layer) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.containingLayer = layer;
    }

    public TileType getType() {
        return type;
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

    public Tile getRelativeTile(Vec2 addLocation) {
        return containingLayer.getTileAt(x + addLocation.x, y + addLocation.y);
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
        return world.getTiledData().findTileSetFromID(type.getID()).getTileProperty(this, property);
    }

    public boolean hasProperty(String property, World world) {
        return getProperty(property, world) != null;
    }
}
