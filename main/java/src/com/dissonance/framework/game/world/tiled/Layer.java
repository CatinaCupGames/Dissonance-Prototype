package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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
    private ConcurrentHashMap<Integer, Boolean[]> fuckItInTheAsshole = new ConcurrentHashMap<Integer, Boolean[]>();

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
        if (fuckItInTheAsshole.containsKey(index)) {
            Boolean[] values = fuckItInTheAsshole.get(index);
            Tile t = new Tile(data[index], x, y, this, world, values[0], values[1], values[2]);
            cache.put(index, t);
            return t;
        } else {
            boolean[] values = stripTileRotationFlag(index); //Lets try to start a strip dance it then
            Tile t = new Tile(data[index], x, y, this, world, values[0], values[1], values[2]);
            cache.put(index, t);
            return t;
        }
    }

    private int gLayer = Integer.MIN_VALUE;
    public int getGameLayer(World world) {
        if (gLayer == Integer.MIN_VALUE) {
            if (!isGroundLayer()) {
                Layer high = world.getHighestGroundLayer();
                Layer low = world.getLowestGroundLayer();
                if (high.getLayerNumber() == getLayerNumber() || low.getLayerNumber() == getLayerNumber()) {
                    gLayer = 0;
                    return 0; //Well fuck you to
                }

                if (high == null || low == null)
                    throw new InvalidParameterException("There is no ground layer in this map!");
                if (high.getLayerNumber() < getLayerNumber())
                    gLayer = (getLayerNumber() - high.getLayerNumber()) + 1;
                else if (low.getLayerNumber() > getLayerNumber())
                    gLayer = getLayerNumber() - low.getLayerNumber();
                else
                    throw new InvalidParameterException("There is a non-ground layer in between 2 ground layers! (INVALID LAYER: " + getLayerNumber() + " WORLD: " + world.getName() + ")");
            } else
                gLayer = 0;
        }
        return gLayer;
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

    public Boolean[] stripTileRotationFlag(int index) {
        if (fuckItInTheAsshole.containsKey(index))
            return fuckItInTheAsshole.get(index);

        long id = data[index];
        //Check the rotation flags
        boolean flipH = (id & FLIPPED_HORIZONTALLY_FLAG) > 0;
        boolean flipL = (id & FLIPPED_VERTICALLY_FLAG)   > 0;
        boolean flipD = (id & FLIPPED_DIAGONALLY_FLAG)   > 0;

        //Clear the rotation flags
        data[index] &= ~(FLIPPED_HORIZONTALLY_FLAG | FLIPPED_VERTICALLY_FLAG | FLIPPED_DIAGONALLY_FLAG);

        if (flipH || flipL || flipD)
            fuckItInTheAsshole.put(index, new Boolean[] { flipH, flipL, flipD });

        return new Boolean[] { flipH, flipL, flipD };
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

    public void setAlpha(float alpha) {
        this.opacity = alpha;
    }
}
