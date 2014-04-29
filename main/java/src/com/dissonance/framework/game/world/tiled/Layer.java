package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class Layer {
    private static final int FLIPPED_HORIZONTALLY_FLAG = 0x80000000;
    private static final int FLIPPED_VERTICALLY_FLAG   = 0x40000000;
    private static final int FLIPPED_DIAGONALLY_FLAG   = 0x20000000;

    private int height;
    private int width;
    private String name;
    private float opacity;
    private boolean visible;
    private int x;
    private int y;
    private String type;
    private HashMap<Object, Object> properties;
    private String image; //for type imagelayer
    private TiledObject[] objects; //for type objectgroup
    private long[] data; //for type tilelayer
    private HashMap<Integer, Tile> cache = new HashMap<Integer, Tile>();

    private int layer_number;

    void setLayerNumber(int number) {
        this.layer_number = number;
    }

    public Tile getTileAt(float x, float y, World world) {
        if (!isTiledLayer())
            throw new InvalidParameterException("This layer is not a Tile Layer!");

        int index = (int) (x + (y * width));

        if (cache.containsKey(index))
            return cache.get(index);

        if (index < 0 || index >= data.length) {
            return null;
        }
        Tile t = new Tile(data[index], x, y, this, world);
        cache.put(index, t);
        return t;
    }


    public boolean isGroundLayer() {
        return (getProperty("ground") != null && getProperty("ground").equalsIgnoreCase("true"));
    }

    public int getLayerNumber() {
        return layer_number;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public float getOpacity() {
        return opacity;
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

    public String getRawType() {
        return type;
    }

    public String getProperty(String key) {
        if (properties == null)
            return null;
        return (String) properties.get(key);
    }

    public boolean isImageLayer() {
        return type.equals("imagelayer");
    }

    public boolean isObjectLayer() {
        return type.equals("objectgroup");
    }

    public boolean isTiledLayer() {
        return type.equals("tilelayer");
    }

    public long[] getTileLayerData() {
        return data;
    }

    public boolean[] stripTileRotationFlag(int index) {
        long id = data[index];
        //Check the rotation flags
        boolean flipH = (id & FLIPPED_HORIZONTALLY_FLAG) > 0;
        boolean flipL = (id & FLIPPED_VERTICALLY_FLAG)   > 0;
        boolean flipD = (id & FLIPPED_DIAGONALLY_FLAG)   > 0;

        //Clear the rotation flags
        data[index] &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);

        return new boolean[] { flipH, flipL, flipD };
    }

    public String getImageLayerData() {
        return image;
    }

    public TiledObject[] getObjectGroupData() {
        return objects;
    }

    public LayerType getLayerType() {
        return LayerType.parse(type);
    }

    public void dispose() {
        if (properties != null)
            properties.clear();
        data = null;
        objects = null;
        properties = null;
    }

    public boolean isParallaxLayer() {
        return (getProperty("parallax") != null && getProperty("parallax").equalsIgnoreCase("true"));
    }
}
