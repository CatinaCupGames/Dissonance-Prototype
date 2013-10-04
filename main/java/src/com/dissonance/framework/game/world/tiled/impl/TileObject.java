package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.world.tiled.DrawableTile;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.render.texture.Texture;
import org.jbox2d.common.Vec2;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class TileObject extends DrawableTile {
    private static final HashMap<Integer, TexCordHolder> cache = new HashMap<Integer, TexCordHolder>();
    private TexCordHolder tex_cords;

    private TileSet parentTileSet;
    private int data_index;
    private final int ID;

    private int x;
    private int y;

    public TileObject(int ID, TileSet parentTileSet, Layer parentLayer, int data_index) {
        super(parentLayer);
        this.ID = ID;
        this.parentTileSet = parentTileSet;
        this.data_index = data_index;

        x = data_index % parentLayer.getWidth();
        y = data_index / parentLayer.getHeight();
        y++;
    }

    @Override
    public void init() {
        if (cache.containsKey(ID)) {
            tex_cords = cache.get(ID);
        } else if (parentTileSet.getTexture() != null) {
            tex_cords = new TexCordHolder();
            tex_cords.bottom_left = parentTileSet.getTexture().getTextureCord(Texture.BOTTOM_LEFT, ID, parentTileSet);
            tex_cords.bottom_right = parentTileSet.getTexture().getTextureCord(Texture.BOTTOM_RIGHT, ID, parentTileSet);
            tex_cords.top_left = parentTileSet.getTexture().getTextureCord(Texture.TOP_LEFT, ID, parentTileSet);
            tex_cords.top_right = parentTileSet.getTexture().getTextureCord(Texture.TOP_RIGHT, ID, parentTileSet);

            cache.put(ID, tex_cords);
        }
    }

    @Override
    public float getX() {
        return x * parentTileSet.getTileWidth();
    }

    @Override
    public float getY() {
        return y * parentTileSet.getTileHeight();
    }

    public int getIndex() {
        return data_index;
    }

    @Override
    public void update() { }

    @Override
    public void render() {
        if (parentTileSet.getTexture() == null)
            return;
        super.render();

        parentTileSet.getTexture().bind();
        float bx = parentTileSet.getTileWidth() / 2;
        float by = parentTileSet.getTileHeight() / 2;
        float x = getX(), y = getY();

        glBegin(GL_QUADS);
        glTexCoord2f(tex_cords.bottom_left.x, tex_cords.bottom_left.y); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(tex_cords.bottom_right.x, tex_cords.bottom_right.y); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(tex_cords.top_right.x, tex_cords.top_right.y); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(tex_cords.top_left.x, tex_cords.top_left.y); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        parentTileSet.getTexture().unbind();

        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static class TexCordHolder {
        public Vec2 top_left;
        public Vec2 top_right;
        public Vec2 bottom_left;
        public Vec2 bottom_right;
    }
}
