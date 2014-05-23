package com.dissonance.game.triggers;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.game.quests.GameQuest;

public class ConveyorBeltFactory extends ConveyorBelt {

    @Override
    public void onTrigger(AnimatedSprite sprite, Tile tile) {
        if (GameQuest.INSTANCE.factory_beltsactive && sprite.getLayer() == 2) {
            super.onTrigger(sprite, tile);
        }
    }
}
