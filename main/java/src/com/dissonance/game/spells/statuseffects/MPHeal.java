package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class MPHeal extends StatusEffect {
    public MPHeal(long duration, double value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {

    }

    @Override
    protected void onInflict(CombatSprite owner) {
        owner.setMP(owner.getMP() + super.value);
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
