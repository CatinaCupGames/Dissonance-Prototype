package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class Layer {
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
    private int[] data; //for type tilelayer

    private int layer_number;

    void setLayerNumber(int number) {
        this.layer_number = number;
    }

    public Tile getTileAt(float x, float y, World world) {
        if (!isTiledLayer())
            throw new InvalidParameterException("This layer is not a Tile Layer!");

        int index = (int) (x + (y * height));

        if (index < 0 || index >= data.length) {
            System.out.println("Invalid index specified");
            return null;
        }
        return new Tile(data[index], x, y, this, world);
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

    public int[] getTileLayerData() {
        return data;
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
}
