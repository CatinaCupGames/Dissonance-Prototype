package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;

public final class Wyatt extends PlayableSprite {

    @Override
    public void onLevelUp() {
        //TODO Ask for stat things? idk
    }

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
    public void setAttack(int attack) {

    }

    @Override
    public void setDefense(int defense) {

    }

    @Override
    public CombatType getCombatType() {
        return CombatType.HUMAN;
    }

    @Override
    public void setSpeed(int speed) {

    }

    @Override
    public void setVigor(int vigor) {

    }

    @Override
    public String getSpriteName() {
        return "Wyatt";
    }
}
