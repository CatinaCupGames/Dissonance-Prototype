package com.dissonance.framework.render.texture.tile;

import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.render.texture.Texture;
import org.lwjgl.util.vector.Vector2f;

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

    public float[] convertToCords(int pos) {
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
            if (temp == pos)
                break;
        }

        return new float[] { x, y };
    }

    @Override
    public int getTextureWidth() {
        return tileWidth * 2;
    }

    @Override
    public int getTextureHeight() {
        return tileHeight * 2;
    }

    public Vector2f getTextureCord(int pos, int id, TileSet tile) {
        if (!tile.containsID(id))
            throw new InvalidParameterException("The TileSet provided does not contain the tile \"" + id + "\"");
        int tilepos = (id - tile.getFirstGrid()) + 1; //Returns the tile id of this tile, relative to tileset.

        float x, y;
        float[] temp = convertToCords(tilepos);

        x = temp[0];
        y = temp[1];
        if (pos == 3) { //Bottom left
            y += tileHeight;
        } else if (pos == 2) { //Bottom right
            y += tileHeight;
            x += tileWidth;
        } else if (pos == 1) { //Top right
            x += tileWidth;
        }  else if (pos != 0) { //We start with the Top Left, ignore pos 3
            throw new InvalidParameterException("The parameter \"type\"'s value can only be 0, 1, 2, or 3");
        }

        x /= super.getTextureWidth(); //Convert to fraction
        y /= super.getTextureHeight(); //Convert to fraction

        return new Vector2f(x, y);
    }
}
