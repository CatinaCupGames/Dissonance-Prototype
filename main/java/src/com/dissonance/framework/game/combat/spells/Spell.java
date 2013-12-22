package com.dissonance.framework.game.combat.spells;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public interface Spell {

    public void castSpell();

    public int mpCost();

    public CombatSprite getOwner();
}
