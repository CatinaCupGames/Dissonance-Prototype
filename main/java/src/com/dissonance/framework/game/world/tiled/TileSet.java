package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.tile.TileTexture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TileSet {
    private int firstgid;
    private String image;
    private int imageheight;
    private int imagewidth;
    private int margin;
    private String name;
    private int spacing;
    private int tileheight;
    private int tilewidth;
    private HashMap<Object, Object> properties;
    private static HashMap<String, TextureData> textures = new HashMap<String, TextureData>();
    private TileTexture texture;

    public String getProperty(String key) {
        return (String) properties.get(key);
    }

    public void loadTexture() {
        if (textures.containsKey(image)) {
            TextureData td = textures.get(image);
            texture = td.texture;
            td.users.add(this); //Register yourself as a user of this texture
        } else {
            Texture temp = null;
            try {
                temp = Texture.retriveTexture(image);
            } catch (IOException e) {
                System.out.println("[WARNING] Texture not found for TileSet \"" + name + "\" (\"" + image + "\") ! ");
                return;
            }
            if (temp == null) {
                System.out.println("[WARNING] Texture not found for TileSet \"" + name + "\" (\"" + image + "\") ! ");
                return;
            }
            texture = new TileTexture(temp, tilewidth, tileheight, spacing, margin, getTilesPerRow(), getRowCount());
            Texture.replaceTexture(temp, texture);

            TextureData td = new TextureData();
            td.texture = texture;
            td.users.add(this); //Register yourself as a user of this texture

            textures.put(image, td);
        }
    }

    public TileTexture getTexture() {
        return texture;
    }

    public boolean containsID(int id) {
        return id >= firstgid && id <= ((getTilesPerRow() * getRowCount()) + (firstgid - 1));
    }

    public int getTilesPerRow() {
        return imagewidth / tilewidth;
    }

    public int getRowCount() {
        return imageheight / tileheight;
    }

    public int getFirstGrid() {
        return firstgid;
    }

    public String getImagePath() {
        return image;
    }

    public int getImageWidth() {
        return imagewidth;
    }

    public int getImageHeight() {
        return imageheight;
    }

    public int getMargin() {
        return margin;
    }

    public String getName() {
        return name;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getTileWidth() {
        return tilewidth;
    }

    public int getTileHeight() {
        return tileheight;
    }

    public void dispose() {
        properties.clear();

        TextureData td = textures.get(image);
        td.users.remove(this); //Unregister myself from the cache

        if (td.users.size() == 0) { //If no one else is using the texture
            textures.remove(image); //Remove it from the cache
            texture.dispose(); //And remove it from memory
        }
    }

    /**
     * All the TileSets that are using this texture.
     * This multiple maps will have multiple instances of the same TileSet, many textures will be repeatedly loaded.
     *
     * This cache ensures that the texture is only loaded once and keeps track of which TileSets are using it.
     *
     * When no TileSets are using it, the texture is unloaded from memory
     */
    private static class TextureData {
        public ArrayList<TileSet> users = new ArrayList<TileSet>();
        public TileTexture texture;
    }
}
