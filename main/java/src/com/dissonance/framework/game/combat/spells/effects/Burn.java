package com.dissonance.framework.game.combat.spells.effects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Burn extends StatusEffect {
    public Burn(long duration, float damage) {
        super(duration, damage);
    }

    @Override
    protected void onInflict(CombatSprite owner) {

    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
