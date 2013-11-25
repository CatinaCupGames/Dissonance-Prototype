package com.dissonance.framework.system.utils;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;

public class HitBox {
    private float minX, maxX, minY, maxY;
    private float x, y;

    public HitBox(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxY = maxY;
        this.maxX = maxX;
        this.x = 0;
        this.y = 0;
    }

    public boolean checkForCollision(World world, Sprite sprite) {
        float sX = sprite.getX() - (sprite.getWidth() / 2);
        float sY = sprite.getY() - (sprite.getHeight() / 2);

        sX += minX;
        sY += minY;

        for (float x = sX; x < sX + maxX; x++) {
            for (float y = sY; y < sY + maxY; y++) {
                if (world.getPolygonsAt(x, y).size() > 0)
                    return true;
                /*else {
                    for (Layer l : world.getLayers(LayerType.TILE_LAYER)) {
                        Tile t = world.getTileAt(x, y, l);
                        if (t != null && !t.isPassable())
                            return true;
                    }
                }*/
            }
        }

        return false;
    }
}
