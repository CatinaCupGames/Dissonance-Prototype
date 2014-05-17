package com.dissonance.game.spells.statuseffects;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

import java.awt.*;

public class HPHeal extends StatusEffect {
    public HPHeal(long duration, float value) {
        super(duration, value);
    }

    @Override
    protected void onStart(CombatSprite owner) {
        inflict(owner);
    }

    @Override
    protected void onInflict(CombatSprite owner) {
        double add = super.value;
        while (owner.getHP() + add > owner.getMaxHP())
            add--;
        owner.setHP(owner.getHP() + add); //Use the value variable as an add modifier
        owner.toastText("+ " + add).setTint(Color.GREEN);
    }

    @Override
    protected void onEnd(CombatSprite owner) { }
}
