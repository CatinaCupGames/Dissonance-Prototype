package com.dissonance.game.sprites;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.spells.impl.Earthquake;

public final class Jeremiah extends PlayableSprite {
    //TODO Set default values for these.
    private int attack;
    private int defense;
    private int speed;
    private int vigor;
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

        if (BossQuest.END) {
            setAnimation("walk_left");
            super.setFrame(1);
            pauseAnimation();
            return;
        }

        String type = "walk";
        if (getMovementType() == MovementType.RUNNING)
            type = "run";

        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                setAnimation(type + "_back");
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                setAnimation(type + "_front");
                break;
            case LEFT:
                setAnimation(type + "_left");
                break;
            case RIGHT:
                setAnimation(type + "_right");
                break;
        }
    }

    @Override
    public void onNoMovement() {
        if (BossQuest.END) {
            setAnimation("walk_left");
            super.setFrame(1);
            pauseAnimation();
            return;
        }
        if (isMoving() || ignore_movement) {
            return;
        }
        if (getMovementType() == MovementType.RUNNING) {
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
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        pauseAnimation();

        setAttack(16);
        setDefense(8);
        setSpeed(8);
        setVigor(8);
        setStamina(120);
        setMaxStamina(120);
        setWillpower(10);
        setFocus(4);
    }

    public void giveSpells() {
        addSpell(new Earthquake(this));
        setSpell1(getSpell("Earthquake"));
    }

    @Override
    public String getSpriteName() {
        return "jeremiah";
    }
}
