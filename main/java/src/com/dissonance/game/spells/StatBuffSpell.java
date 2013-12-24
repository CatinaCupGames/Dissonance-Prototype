package com.dissonance.game.spells;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.game.spells.statuseffects.StatBuff;

public abstract class StatBuffSpell implements Spell {
    private CombatSprite owner;
    public StatBuffSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        owner.applyStatusCondition(getStatBuffEffect());
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    public abstract StatBuff getStatBuffEffect();
}
