package com.dissonance.framework.render.texture.tile;

import com.dissonance.framework.render.texture.Texture;
import org.jbox2d.common.Vec2;
import tiled.core.Tile;

public class TileTexture extends Texture {

    private int tileWidth;
    private int tileHeight;
    private int spacing;
    private int margin;
    private int perrow;
    private int size;
    protected TileTexture(int i1, int i2) {
        super(i1, i2);
    }

    protected TileTexture(Texture texture) {
        super(texture);
    }

    public TileTexture(Texture texture, int tileWidth, int tileHeight, int spacing, int margin, int perrow) {
        super(texture);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;
        this.margin = margin;
        this.perrow = perrow;
        this.size = getSize();
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
        int y = (tilepos / size) * tileHeight;

        return new Vec2(0, 0);
    }
}
