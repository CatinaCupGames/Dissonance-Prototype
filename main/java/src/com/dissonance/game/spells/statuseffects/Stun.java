package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Stun extends StatusEffect {
    int oS, oV;
    public Stun(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            return;
        int constant = 8;
        if (owner.getCombatType() == CombatSprite.CombatType.MACHINE)
            constant = 4;
        oS = owner.getSpeed();
        oV = owner.getVigor();
        owner.setSpeed(owner.getSpeed() - (owner.getSpeed() / constant));
        owner.setVigor(owner.getVigor() - (owner.getVigor() / constant));
    }

    @Override
    protected void onInflict(CombatSprite owner) { }

    @Override
    protected void onEnd(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            return;
        owner.setSpeed(oS);
        owner.setVigor(oV);
    }
}
