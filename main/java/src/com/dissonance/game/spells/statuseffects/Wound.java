package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Wound extends StatusEffect {
    public Wound(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {

    }

    @Override
    protected void onInflict(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.MACHINE)
            return;
        double damage = 3;
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            damage = 7;


        double willpower = owner.getWillPower();
        double focus = super.value; //Use the parent's value variable as the orgin's focus stat
        double Tdamage = (Math.pow((willpower / focus), (1.0/3.0)) * damage) * 2;
        if (Tdamage > 100)
            Tdamage = 100;

        owner.applyDamage(Tdamage);
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
