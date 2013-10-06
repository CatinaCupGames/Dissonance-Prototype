package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.tiled.impl.ImageLayer;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (properties == null)
            return null;
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
            if (id >= tileSet.getFirstGrid()) {
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

    public void assignAllLayers() {
        for (int i = 0; i < layers.length; i++) {
            layers[i].setLayerNumber(i);
        }
    }

    public List<Drawable> createDrawables() {
        ArrayList<Drawable> tiles = new ArrayList<Drawable>();
        for (Layer layer : layers) {
            if (layer.isTiledLayer()) {
                for (int i = 0; i < layer.getTileLayerData().length; i++) {
                    int id = layer.getTileLayerData()[i];
                    if (id == 0)
                        continue;
                    TileSet set = findTileSetFromID(id);
                    TileObject t = new TileObject(id, set, layer, i);
                    tiles.add(t);
                    t.init();
                }
            } else if (layer.isImageLayer()) {
                final ImageLayer il = new ImageLayer(layer);
                tiles.add(il);
                il.init();
            }
        }

        return tiles;
    }

    public void dispose() {
        properties.clear();
        for (Layer l : layers) {
            l.dispose();
        }

        for (TileSet sets : tilesets) {
            sets.dispose();
        }
    }
}
