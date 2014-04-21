package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.game.world.tiled.impl.ImageLayer;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.texture.TextureLoader;

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

    public int getPixelWidth() {
        return width * tilewidth;
    }

    public int getPixelHeight() {
        return height * tileheight;
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
        boolean old = TextureLoader.isFastRedraw();
        TextureLoader.setFastRedraw(false);
        for (TileSet tileSet : tilesets) {
            tileSet.loadTexture();
        }
        TextureLoader.setFastRedraw(old);
    }

    public void assignAllLayers() {
        for (int i = 0; i < layers.length; i++) {
            layers[i].setLayerNumber(i + 1);
        }
    }

    public List<Drawable> createDrawables(World w) {
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
                    t.setWorld(w);
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

    public void loadTriggers() {
        for (Layer layer : layers) {
            if (layer.isObjectLayer()) {
                for (TiledObject object : layer.getObjectGroupData()) {
                    if (object.isTrigger()) {
                        String class_ = object.getProperty("triggerclass");
                        if (class_ != null) {
                            try {
                                Class<?> trigger = Class.forName(class_);
                                AbstractTrigger trigger1 = (AbstractTrigger) trigger.newInstance();
                                trigger1.init(object);
                                object.attachTrigger(trigger1);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
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
