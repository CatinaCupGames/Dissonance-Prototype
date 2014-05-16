package com.dissonance.game.ai;

import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class MeleeAI implements Behavior {
    private CombatSprite owner;

    public MeleeAI(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void update() {

    }

    public PlayableSprite getClosestPlayer() {
        float distance = owner.getMarksmanship() * 16f;

        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        PlayableSprite closet = null;
        float dis = 0;
        for (PlayableSprite sprite : sprites) {
            float temp = (float) Math.sqrt(((owner.getX() - sprite.getX()) * (owner.getX() - sprite.getX())) + ((owner.getY() - sprite.getY()) * (owner.getY() - sprite.getY())));
            if (temp <= distance) {
                if (temp < dis || closet == null) {
                    dis = temp;
                    closet = sprite;
                }
            }
        }

        return closet;
    }
}
