package com.dissonance.game.sprites;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.utils.Direction;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;

import java.awt.*;
import java.awt.Font;

public final class Farrand extends PlayableSprite {
    //TODO Set default values for these
    private int attack;
    private int defense;
    private int speed;
    private int vigor;
    private int stamina;
    private int willpower;
    private int focus;
    private int marksmanship;
    private int magicResistance;

    @Override
    public void onLevelUp() {
        //TODO Promt the user to do something..or maybe nothing at all...?
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getVigor() {
        return vigor;
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public int getWillPower() {
        return willpower;
    }

    @Override
    public int getFocus() {
        return focus;
    }

    @Override
    public int getMarksmanship() {
        return marksmanship;
    }

    @Override
    public int getMagicResistance() {
        return magicResistance;
    }

    @Override
    public void setAttack(int attack) {
        this.attack = attack;
    }

    @Override
    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.HUMAN;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setVigor(int vigor) {
        this.vigor = vigor;
    }

    @Override
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    @Override
    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    @Override
    public void setFocus(int focus) {
        this.focus = focus;
    }

    @Override
    public void setMarksmanship(int marksmanship) {
        this.marksmanship = marksmanship;
    }

    @Override
    public void setMagicResistance(int magicResistance) {
        this.magicResistance = magicResistance;
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
        if (InputKeys.isButtonPressed(InputKeys.MOVEUP) || InputKeys.isButtonPressed(InputKeys.MOVEDOWN) || InputKeys.isButtonPressed(InputKeys.MOVELEFT) || InputKeys.isButtonPressed(InputKeys.MOVERIGHT)) {
            return;
        }
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        pauseAnimation();
    }

    @Override
    public String getSpriteName() {
        return "farrand";
    }
}
