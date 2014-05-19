package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;

import java.awt.*;

public class Burn extends StatusEffect {
    int oAttack, oDefense;
    public Burn(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {
        int constant = 8;
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            return; //Humans don't get a stat drop
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            constant = 4;
        oAttack = owner.getAttack();
        oDefense = owner.getDefense();
        owner.setAttack(owner.getAttack() - (owner.getAttack() / constant));
        owner.setDefense(owner.getDefense() - (owner.getDefense() / constant));
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        double damage = 3;
        if (owner.getCombatType() == CombatSprite.CombatType.HUMAN)
            damage = 1;
        if (owner.getCombatType() == CombatSprite.CombatType.CREATURE)
            damage = 7;

        double willpower = owner.getWillPower();
        double focus = super.value; //Use the parent's value variable as the origin's focus stat
        double Tdamage = (Math.pow((willpower / focus), (1.0/3.0)) * damage) * 2;
        if (Tdamage > 100)
            Tdamage = 100;

        Tdamage = FastMath.fastRound((float) Tdamage);

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
