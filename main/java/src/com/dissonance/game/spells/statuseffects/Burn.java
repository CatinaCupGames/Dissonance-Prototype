package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Burn extends StatusEffect {
    int oAttack, oDefense;
    public Burn(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            return;
        int constant = 8;
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            constant = 4;
        oAttack = owner.getAttack();
        oDefense = owner.getDefense();
        owner.setAttack(owner.getAttack() - (owner.getAttack() / constant));
        owner.setDefense(owner.getDefense() - (owner.getDefense() / constant));
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            return;
        double damage = 3;
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            damage = 7;


        double willpower = owner.getWillPower();
        double focus = super.value; //Use the parent's value variable as the orgin's focus stat
        double Tdamage = (Math.pow((willpower / focus), (1.0/3.0)) * damage) * 2;
        if (Tdamage > 100)
            Tdamage = 100;

        owner.applyDamage(Tdamage);
    }

    @Override
    protected void onEnd(CombatSprite owner) {
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            return;
        owner.setAttack(oAttack);
        owner.setDefense(oDefense);
    }
}
