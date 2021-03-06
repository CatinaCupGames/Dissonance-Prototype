package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.ai.behaviors.Flee;
import com.dissonance.framework.game.ai.behaviors.Idle;
import com.dissonance.framework.game.ai.behaviors.Seek;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.system.utils.Direction;

import java.awt.*;

/*
This is the temp melee guard.
TODO rename later
 */
public final class Wyatt extends PlayableSprite {
    //TODO Set default values for these
    private int attack = 6;
    private int defense = 10;
    private int speed = 8;
    private int vigor = 10;
    private int stamina = 6;
    private int willpower = 14;
    private int focus = 16;
    private int marksmanship;

    @Override
    public void onAttack() {
        ParticleSprite.createParticlesAt(getX(), getY(), 3, 5f, new Color(0.856f, 0.94f, 0.968f, 1f), getWorld());
    }

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
    public void onMovement(Direction direction) {
        if (ignore_movement)
            return;
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
        if (isMoving() || ignore_movement) {
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
        return "wyatt";
    }
}
