package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.game.sprites.ImagePhysicsSprite;

public class SmallCrate extends ImagePhysicsSprite implements UpdatableDrawable {
    public SmallCrate() {
        super("sprites/img/Crate_small.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }

    @Override
    public void init() { }

    private int step = 0;
    @Override
    public void update() {
        Layer[] layers = getWorld().getLayers(LayerType.TILE_LAYER);
        for (Layer layer : layers) {
            Tile tile = getWorld().getTileAt(x / 16f, FastMath.fastCeil((y + 8f) / 16f), layer);
            if (tile != null && tile.isTriggerTile())
                tile.getTrigger().onSpriteCollide(this, tile);
        }
        if (getX() > 928.0f && step == 0) {
            setX(928.0f);
            setY(352.0f);
            step++;
        } else if (getX() <= 628.0f && step == 1) {
            setX(928.0f);
            setY(480.0f);
            step++;
        } else if (getX() < 16f && step == 2) {
            setX(0f);
            setY(1264.0f);
            step = 0;
        }
    }
}
