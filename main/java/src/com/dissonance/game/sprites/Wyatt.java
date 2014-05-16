package com.dissonance.game.sprites;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.system.utils.Direction;

public final class Wyatt extends Enemy {

    public Wyatt(String spriteName, StatType statType, CombatType combatType) {
        super("wyatt", StatType.NON_MAGIC, CombatType.HUMAN);
    }

    @Override
    public void onMovement(Direction direction) {
        if (isAnimationPaused()) {
            super.setFrame(2);
            playAnimation();
        }
    }

    @Override
    public void onNoMovement() {
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        pauseAnimation();
    }
}
