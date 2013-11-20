package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.tile.TileTexture;

import java.io.IOException;
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
    private HashMap<Object, HashMap<Object, Object>> tileproperties;
    private TileTexture texture;

    public String getProperty(String key) {
        if (properties == null)
            return null;
        return (String) properties.get(key);
    }

    public void loadTexture() {
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

    public String getTileProperty(int ID, String property) {
        if (tileproperties == null)
            return null;
        ID -= firstgid;
        if (tileproperties.containsKey("" + ID)) {
            return (String) tileproperties.get("" + ID).get(property);
        }
        return null;
    }

    public String getTileProperty(Tile t, String property)  {
        return getTileProperty(t.getID(), property);
    }

    public boolean tileHasProperty(Tile t, String property) {
        return getTileProperty(t, property) != null;
    }

    public boolean tileHasProperty(int ID, String property) {
        return getTileProperty(ID, property) != null;
    }

    public void dispose() {
        properties.clear();
    }
}
