package com.dissonance.framework.game.combat.spells.impl;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.effects.HPHeal;
import com.dissonance.framework.game.combat.spells.effects.MPHeal;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public abstract class CureSpell implements Spell {
    protected CombatSprite owner;

    public CureSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    @Override
    public void castSpell() {
        if (getHPAdd() != 0) {
            HPHeal effect = new HPHeal(getDuration(), getHPAdd());
            owner.applyStatusCondition(effect);
        }
        if (getMPAdd() != 0) {
            MPHeal effect = new MPHeal(getDuration(), getMPAdd());
            owner.applyStatusCondition(effect);
        }
    }

    public abstract int getHPAdd();

    public abstract int getMPAdd();

    public abstract long getDuration();
}
