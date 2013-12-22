package com.dissonance.framework.game.combat.spells.effects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class HPHeal extends StatusEffect {
    public HPHeal(long duration, float damage) {
        super(duration, damage);
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        owner.setHP(owner.getHP() + super.damage); //Use the damage variable as an add modifier
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
