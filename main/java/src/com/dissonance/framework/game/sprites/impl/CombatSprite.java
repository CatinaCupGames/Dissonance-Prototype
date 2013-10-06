package com.dissonance.framework.game.sprites.impl;

public abstract class CombatSprite extends AbstractWaypointSprite {
    //==FIXED STATS==//
    private double HP = 100; //This is a fixed stat

    //==VARIABLE STATS==//
    protected int level = 1;

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
        onLevelUp();
    }

    public abstract void onLevelUp();

    /**
     * Determines the damage a physical attack by the player will deal,
     * as well as the knockback the attack will cause to the enemy
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The attack stat, otherwise 0
     */
    public abstract int getAttack();

    /**
     * Determines the damage taken by incoming attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The defense stat, otherwise 0
     */
    public abstract int getDefense();

    /**
     * Determines movement speed and the ability to dodge attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The speed stat, otherwise 0
     */
    public abstract int getSpeed();

    /**
     * Determines how high the critical hit rate is for the player's
     * attacks, as well as how low the critical hit rate is for attacks against
     * the player. Also determines the player's chance to survive an otherwise
     * fatal blow
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The vigor stat, otherwise 0
     */
    public abstract int getVigor();

    /**
     * Used to perform dodges and special abilities, depletes with use and replenishes over time
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The stamina stat, otherwise 0
     */
    public abstract int getStamina();

    /**
     * Determines the damage magic attacks deal, as well as the
     * damage received from magic attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The willpower stat, otherwise 0
     */
    public abstract int getWillPower();

    /**
     * Determines the damage absorbed by magic shields, the HP
     * replenished by cure magic, the duration of spells' effects, and the
     * effectiveness of buff/debuff spells
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The focus stat, otherwise 0
     */
    public abstract int getFocus();

    /**
     * Determines recoil, reload speed, and jam frequency when
     * using guns
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The marksmanship stat, otherwise 0
     */
    public abstract int getMarksmanship();

    /**
     * For non-magic users, determines damage dealt by incoming magic attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The magic resistance stat, othwerwise 0
     */
    public abstract int getMagicResistance();

    /**
     * Get the {@link CombatType} of this Sprite. The {@link CombatType} determines
     * what Status Conditions are affected by this CombatSprite.
     *
     * @return
     *        {@link CombatType}
     */
    public abstract CombatType getCombatType();



    public double getHP() {
        return HP;
    }


    public enum CombatType {
        /**
         * Any thing that the player can play as
         */
        ALLY,

        /**
         * Hostile things that aren't human
         */
        CREATURE,

        /**
         * Human military units
         */
        TROOP,

        /**
         * Lack the organic carbon-based compounds and souls that we
         * privileged folks possess
         */
        MACHINE
    }
}
