package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Wound extends StatusEffect {
    public Wound(long duration, float damage) {
        super(duration, damage);
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.MACHINE)
            return;
        double damage = 3;
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            damage = 7;

        //TODO Test this equation to see if its fair or not..
        double defense = owner.getFocus();
        double attack = super.damage + damage;
        double Tdamage;
        Tdamage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
        if (Tdamage > 100)
            Tdamage = 100;

        owner.applyDamage(Tdamage);
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
