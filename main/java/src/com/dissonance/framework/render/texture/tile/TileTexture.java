package com.dissonance.framework.render.texture.tile;

import com.dissonance.framework.render.texture.Texture;

public class TileTexture extends Texture {

    private int tileWidth;
    private int tileHeight;
    private int spacing;
    private int margin;
    private int perrow;
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
    }


}
