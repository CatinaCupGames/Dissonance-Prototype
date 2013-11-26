package com.dissonance.framework.system.utils;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;

public class HitBox {
    private float minX, maxX, minY, maxY;
    private float x, y;

    public HitBox(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxY = maxY;
        this.maxX = maxX;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean checkForCollision(World world) {
        return checkForCollision(world, x, y);
    }

    public boolean checkForCollision(World world, Sprite sprite) {
        float height;
        if (sprite.getTexture() instanceof SpriteTexture) {
            SpriteTexture temp = (SpriteTexture)sprite.getTexture();
            height = temp.getHeight();
        } else {
            height = sprite.getHeight();
        }

        float sX = sprite.getX();
        float sY = sprite.getY() + (height / 4.0f);

        sX += minX;
        sY += minY;

        return checkForCollision(world, sX, sY);
    }

    public boolean checkForCollision(World world, float startX, float startY) {
        for (float x = startX; x < startX + (maxX - minX); x++) {
            for (float y = startY; y < startY + (maxY - minY); y++) {
                if (world.getPolygonsAt(x, y).size() > 0)
                    return true;
                else {
                    for (Layer l : world.getLayers(LayerType.TILE_LAYER)) {
                        Tile t = world.getTileAt(x, y, l);
                        if (t != null && !t.isPassable())
                            return true;
                    }
                }
            }
        }

        return false;
    }
}
