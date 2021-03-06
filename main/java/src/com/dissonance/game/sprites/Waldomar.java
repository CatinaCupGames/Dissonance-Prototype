package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.system.utils.Direction;

public class Waldomar extends CombatSprite {
    @Override
    public void onLevelUp() {

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
    public void setAttack(int attack) {

    }

    @Override
    public void setDefense(int defense) {

    }

    @Override
    public CombatType getCombatType() {
        return null;
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return false;
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
    public boolean setAnimation(String name) {
        return super.setAnimation(name);
    }

    @Override
    public boolean setAnimation(int index) {
        return super.setAnimation(index);
    }

    @Override
    public String getSpriteName() {
        return "waldomar";
    }

    @Override
    public void onMovement(Direction direction) {
        if (isAnimationPaused()) {
            super.setFrame(2);
            playAnimation();
        }

        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                setAnimation("walk_back");
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                setAnimation("walk_front");
                break;
            case LEFT:
                setAnimation("walk_left");
                break;
            case RIGHT:
                setAnimation("walk_right");
                break;
        }
    }

    @Override
    public void onNoMovement() {
        if (isMoving()) {
            return;
        }
        super.setFrame(1);
        pauseAnimation();
    }
}
