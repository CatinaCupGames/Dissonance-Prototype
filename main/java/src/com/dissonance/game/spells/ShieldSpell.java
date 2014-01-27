package com.dissonance.game.spells;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.game.spells.statuseffects.HPHeal;
import com.dissonance.game.spells.statuseffects.MPHeal;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class ShieldSpell {
    protected CombatSprite owner;

    public ShieldSpell(CombatSprite owner) {
        this.owner = owner;
    }

    /*@Override
    public CombatSprite getOwner() {
        return owner;
    }

    @Override
    public void castSpell() {
    }*/
}
