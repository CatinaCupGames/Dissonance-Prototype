package com.dissonance.game.spells;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public abstract class BoltSpell implements Spell {
    protected CombatSprite owner;

    public BoltSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {

    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    public abstract StatusEffect getEffect();
}
