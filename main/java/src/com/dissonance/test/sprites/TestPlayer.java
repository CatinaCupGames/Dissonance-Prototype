package com.dissonance.test.sprites;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.system.utils.Direction;

public class TestPlayer extends PlayableSprite {

    @Override
    public void onLoad() {
        super.onLoad();
        setFrame(0);
        pauseAnimation();
        Weapon w = Weapon.getWeapon("test");
        setCurrentWeapon(w.createItem(this));
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void onMovement(Direction direction) {
        playAnimation();
    }

    @Override
    public void onNoMovement() {
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void setSpeed(int speed) {

    }

    @Override
    public void setVigor(int vigor) {

    }

    @Override
    public void setStamina(int stamina) {

    }

    @Override
    public void setWillpower(int willpower) {

    }

    @Override
    public void setFocus(int focus) {

    }

    @Override
    public void setMarksmanship(int marksmanship) {

    }

    @Override
    public void setMagicResistance(int magicResistance) {

    }

    @Override
    public void onDeselect() {
        super.onDeselect();
        setFrame(0);
        pauseAnimation();
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public void onLevelUp() {
    }

    @Override
    public int getAttack() {
        return 30;
    }

    @Override
    public int getDefense() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 90;
    }

    @Override
    public int getVigor() {
        return 1;
    }

    @Override
    public int getStamina() {
        return 1;
    }

    @Override
    public int getWillPower() {
        return 1;
    }

    @Override
    public int getFocus() {
        return 1;
    }

    @Override
    public int getMarksmanship() {
        return 5;
    }

    @Override
    public int getMagicResistance() {
        return 1;
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
}
