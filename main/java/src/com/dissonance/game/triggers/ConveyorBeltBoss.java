package com.dissonance.game.triggers;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.quests.GameQuest;

public class ConveyorBeltBoss extends ConveyorBelt {

    @Override
    public void onTrigger(AnimatedSprite sprite, Tile tile) {
        if (!BossQuest.END) {
            super.onTrigger(sprite, tile);
        }
    }

    @Override
    public void onSpriteTrigger(Sprite sprite, Tile tile) {
        if (!BossQuest.END) {
            super.onSpriteTrigger(sprite, tile);
        }
    }
}
