package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class HPHeal extends StatusEffect {
    public HPHeal(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {

    }

    @Override
    protected void onInflict(CombatSprite owner) {
        owner.setHP(owner.getHP() + super.value); //Use the value variable as an add modifier
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
