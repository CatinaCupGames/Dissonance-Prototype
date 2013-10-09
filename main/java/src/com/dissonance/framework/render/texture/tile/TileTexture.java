package com.dissonance.framework.render.texture.tile;

import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.render.texture.Texture;
import org.jbox2d.common.Vec2;

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
        this(texture, tileWidth, tileHeight, spacing, margin, perrow, 0);
        this.size = getSize();
    }

    public TileTexture(Texture texture, int tilewidth, int tileheight, int spacing, int margin, int tilesPerRow, int rowCount) {
        super(texture);
        this.tileWidth = tilewidth;
        this.tileHeight = tileheight;
        this.spacing = spacing;
        this.margin = margin;
        this.perrow = tilesPerRow;
        this.size = rowCount;

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

    public int[] convertToCords(int pos) {
        int x = 0;
        int y = 0;
        int temp = 1;
        while (y + tileHeight + margin <= super.getImageHeight()) {
            x += tileWidth + spacing;
            if (x + tileWidth + margin > super.getImageWidth()) {
                x = margin;
                y += tileHeight + spacing;
            }
            temp++;
        }

        return new int[] { x, y };
    }

    @Override
    public int getTextureWidth() {
        return tileWidth * 2;
    }

    @Override
    public int getTextureHeight() {
        return tileHeight * 2;
    }

    public Vec2 getTextureCord(int pos, int id, TileSet tile) {
        if (!tile.containsID(id))
            throw new InvalidParameterException("The TileSet provided does not contain the tile \"" + id + "\"");
        int tilepos = (id - tile.getFirstGrid()) + 1; //Returns the tile id of this tile, relative to tileset.

        float x = (tilepos % perrow) * tileWidth;
        float y = (int)(((tilepos - 1) / perrow)) * tileHeight;

        //zy -= tileHeight;
        x -= tileWidth;

        x /= getImageWidth(); //Convert to fraction
        y /= getImageHeight(); //Convert to fraction

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
