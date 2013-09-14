package com.dissonance.framework.render.texture.tile;

import com.dissonance.framework.render.texture.Texture;
import org.jbox2d.common.Vec2;
import tiled.core.Tile;

import java.security.InvalidParameterException;

public class TileTexture extends Texture {

    private int tileWidth;
    private int tileHeight;
    private int spacing;
    private int margin;
    private int perrow;
    private int size;
    private final float x_fraction;
    private final float y_fraction;
    protected TileTexture(int i1, int i2) {
        super(i1, i2);
        x_fraction = 0.0f;
        y_fraction = 0.0f;
    }

    protected TileTexture(Texture texture) {
        super(texture);
        x_fraction = 0.0f;
        y_fraction = 0.0f;
    }

    public TileTexture(Texture texture, int tileWidth, int tileHeight, int spacing, int margin, int perrow) {
        super(texture);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;
        this.margin = margin;
        this.perrow = perrow;
        this.size = getSize();

        x_fraction = 1.0f / (float)(perrow);
        y_fraction = 1.0f / (float)(size);
    }

    private int getSize() {
        int x = 0;
        int y = 0;
        int temp = 1;
        while (y + tileHeight + margin <= super.getImageHeight()) {
            x += tileWidth + spacing;
            if (x + tileWidth + margin > super.getImageWidth()) {
                x = margin;
                y += tileHeight + spacing;
                temp++;
            }
        }
        return temp;
    }

    public Vec2 getTextureCord(int pos, Tile tile) {
        int tilepos = tile.getId(); //Returns the tile id of this tile, relative to tileset.

        int x = (tilepos % perrow) * tileWidth;
        int y = (tilepos % size) * tileHeight;

        y += tileHeight; //Get bottom left

        x /= super.getTextureWidth(); //Convert to fraction
        y /= super.getTextureHeight(); //Convert to fraction

        if (pos == 0) { //Bottom left
            return new Vec2(x, y);
        } else if (pos == 1) { //Bottom right
            return new Vec2(x + x_fraction, y);
        } else if (pos == 2) { //Top right
            return new Vec2(x + x_fraction, y + y_fraction);
        } else if (pos == 3) { //Top left
            return new Vec2(x, y + y_fraction);
        } else {
            throw new InvalidParameterException("The parameter \"type\"'s value can only be 0, 1, 2, or 3");
        }
    }
}
