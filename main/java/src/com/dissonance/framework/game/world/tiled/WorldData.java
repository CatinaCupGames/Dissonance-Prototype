package com.dissonance.framework.game.world.tiled;

import java.util.HashMap;

public class WorldData {
    private int height;
    private int tileheight;
    private int tilewidth;
    private int version;
    private int width;
    private HashMap<Object, Object> properties;
    private Layer[] layers;
    private TileSet[] tilesets;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileHeight() {
        return tileheight;
    }

    public int getTileWidth() {
        return tilewidth;
    }

    public int getVersion() {
        return version;
    }

    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    public Layer[] getLayers() {
        return layers;
    }

    public TileSet[] getTilesets() {
        return tilesets;
    }

    public TileSet findTileSetFromID(int id) {
        for (TileSet tileSet : tilesets) {
            if (id > tileSet.getFirstGrid()) {
                if (tileSet.containsID(id)) {
                    return tileSet;
                }
            }
        }
        return null;
    }

    public void loadAllTileSets() {
        for (TileSet tileSet : tilesets) {
            tileSet.loadTexture();
        }
    }
}
