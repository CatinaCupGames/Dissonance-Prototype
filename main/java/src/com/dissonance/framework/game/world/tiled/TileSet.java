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

    public String getGlobalTileProperty(String key) {
        if (properties == null)
            return null;
        return (String) properties.get(key);
    }

    public void loadTexture() {
        Texture temp;
        try {
            temp = Texture.retrieveTexture(image);
        } catch (IOException e) {
            try {
                temp = Texture.retrieveTexture("worlds/" + image);
            } catch (IOException e1) {
                System.out.println("[WARNING] Texture not found for TileSet \"" + name + "\" (\"" + image + "\") ! ");
                return;
            }
        }
        if (temp == null) {
            try {
                temp = Texture.retrieveTexture("worlds/" + image);
                if (temp == null)
                    throw new IOException("Go to catch block pls"); //Living the easy life
            } catch (IOException e1) {
                System.out.println("[WARNING] Texture not found for TileSet \"" + name + "\" (\"" + image + "\") ! ");
                return;
            }
        }
        texture = new TileTexture(temp, tilewidth, tileheight, spacing, margin, getTilesPerRow(), getRowCount());
        Texture.replaceTexture(temp, texture);
    }

    public TileTexture getTexture() {
        return texture;
    }

    public boolean containsID(long id) {
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

    public String getTileProperty(long ID, String property) {
        if (tileproperties == null)
            return getGlobalTileProperty(property);
        ID -= firstgid;
        if (tileproperties.containsKey("" + ID)) {
            String val =  (String) tileproperties.get("" + ID).get(property);
            if (val != null)
                return val;
        }
        return getGlobalTileProperty(property);
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
