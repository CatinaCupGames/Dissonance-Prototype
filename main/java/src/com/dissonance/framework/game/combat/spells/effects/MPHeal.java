package com.dissonance.framework.game.combat.spells.effects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class MPHeal extends StatusEffect {
    public MPHeal(long duration, float damage) {
        super(duration, damage);
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        owner.setMP(owner.getMP() + super.damage);
    }
}
