package com.dissonance.game.triggers;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;

public class Fall extends AbstractTileTrigger {
    @Override
    public void onTrigger(AnimatedSprite sprite, Tile tile) {
        if (LayerSwitch.climbers.contains(sprite) || sprite.getLayer() == 1)
            return;
        sprite.setLayer(2);
    }

    @Override
    public void onSpriteTrigger(Sprite sprite, Tile tile) {
        if (LayerSwitch.climbers.contains(sprite) || sprite.getLayer() == 1)
            return;
        sprite.setLayer(2);
    }
}
