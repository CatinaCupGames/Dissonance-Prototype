package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

import java.util.Random;

public class Enemy extends CombatSprite {
    private static final byte STAT_COUNT = 9;
    private static final Random random = new Random();

    private final String spriteName;
    private final StatType statType;
    private final CombatType combatType;

    private int attack;
    private int defense;
    private int speed;
    private int vigor;
    private int stamina;
    private int willPower;
    private int focus;
    private int marksmanship;

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
        return willPower;
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
        return combatType;
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return false;
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
        this.willPower = willpower;
    }

    @Override
    public void setFocus(int focus) {
        this.focus = focus;
    }

    @Override
    public void setMarksmanship(int marksmanship) {
        this.marksmanship = marksmanship;
    }

    public enum StatType {
        MAGIC, NON_MAGIC
    }

    private static int[] generateStats(int points, StatType type) {
        return getRandomStats(new int[STAT_COUNT], points, type, (points / 4));
    }

    private static int[] getRandomStats(int[] statArray, int points, StatType type, int valueCap) {
        int cap = (int) Math.floor(points / STAT_COUNT);
        if (cap == 0) cap++;

        int mp = points;

        for (int i = 0; i < STAT_COUNT; i++) {
            if ((type == StatType.MAGIC && i > 6) || (type == StatType.NON_MAGIC && (i == 5 || i == 6))) {
                continue;
            }

            int gen = random.nextInt(cap + 1);

            if (statArray[i] + gen > valueCap) {
                continue;
            }

            statArray[i] += gen;
            mp -= gen;
        }

        if (mp > 0) {
            statArray = getRandomStats(statArray, mp, type, valueCap);
        }

        return statArray;
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    public Enemy(String spriteName, StatType statType, CombatType combatType) {
        this.spriteName = spriteName;
        this.statType = statType;
        this.combatType = combatType;
        setLevel(1);
    }

    public Enemy(String spriteName, StatType statType, CombatType combatType, Behavior behavior) {
        this(spriteName, statType, combatType);
        setBehavior(behavior);
    }

    /**
     * Sets this enemy's level to the specified level.
     *
     * @param level The new level.
     */
    public final void setLevel(int level) {
        this.level = level;

        onLevelUp();
    }

    @Override
    public void onLevelUp() {
        int[] stats = generateStats(level * 9, statType);
        attack = stats[0];
        defense = stats[1];
        speed = stats[2];
        vigor = stats[3];
        stamina = stats[4];
        willPower = stats[5];
        focus = stats[6];
        marksmanship = stats[7];
    }

    @Override
    public String toString() {

        return "Enemy.java: [\n" + " name: " + spriteName + "\n level: " + level + "\n" + "[\n" + " attack: " + attack +
                "\n defense: " + defense + "\n speed: " + speed + "\n vigor: " + vigor + "\n stamina: " + stamina +
                "\n willPower: " + willPower + "\n focus: " + focus + "\n marksmanship: " + marksmanship +
                "\n]\n]";
    }

}