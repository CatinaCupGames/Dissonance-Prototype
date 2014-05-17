package com.dissonance.game.spells.impl;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.game.spells.CureSpell;

public class HeavyCure extends CureSpell {
    public HeavyCure(CombatSprite owner) {
        super(owner);
    }

    @Override
    public int getHPAdd() {
        return 20;
    }

    @Override
    public int getMPAdd() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 10000;
    }

    @Override
    public int mpCost() {
        return 20;
    }

    @Override
    public String getName() {
        return "Heavy Cure";
    }
}
