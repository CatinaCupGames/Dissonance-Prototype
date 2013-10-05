package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.PlayableSprite;

public class Wyatt extends PlayableSprite {
    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public int getVigor() {
        return 0;
    }

    @Override
    public int getStamina() {
        return 0;
    }

    @Override
    public int getWillPower() {
        return 0;
    }

    @Override
    public int getFocus() {
        return 0;
    }

    @Override
    public int getMarksmanship() {
        return 0;
    }

    @Override
    public int getMagicResistance() {
        return 0;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.ALLY;
    }

    @Override
    public String getSpriteName() {
        return "Wyatt";
    }
}
