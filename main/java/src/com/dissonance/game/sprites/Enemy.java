package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.CombatSprite;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;

import java.util.Random;

public class Enemy extends CombatSprite {
    private static final byte STAT_COUNT = 9;
    private static final Random random = new Random();

    private final String spriteName;
    private final StatType statType;
    private final CombatType combatType;
    private final AIInterface ai;
    private int level;

    private int attack;
    private int defense;
    private int speed;
    private int vigor;
    private int stamina;
    private int willPower;
    private int focus;
    private int marksmanship;
    private int magicResistance;

    //region Stat getters
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
    public int getMagicResistance() {
        return magicResistance;
    }

    @Override
    public CombatType getCombatType() {
        return combatType;
    }

    @Override
    public void update() {
        super.update();

        ai.onUpdate(this);
    }

    @Override
    public void onSelected(PlayableSprite player) {
        levelUp();
        System.out.println(this);
    }

    //endregion

    public enum StatType {
        MAGIC, NON_MAGIC
    }
    //region Stat generation
    private static int[] generateStats(int points, StatType type) { //in case someone else does enemies, remove docs
        return getRandomStats(new int[STAT_COUNT], points, type, (points / 4)); //when done cause private method
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
    //endregion

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    public Enemy(String spriteName, StatType statType, CombatType combatType, AIInterface ai) {
        this.spriteName = spriteName;
        this.statType = statType;
        this.combatType = combatType;
        this.ai = ai;
        setLevel(1);
    }

    /**
     * Sets this enemy's level to the specified level.
     *
     * @param level The new level.
     */
    public final void setLevel(int level) {
        this.level = level;

        int[] stats = generateStats(level * 9, statType);
        attack = stats[0];
        defense = stats[1];
        speed = stats[2];
        vigor = stats[3];
        stamina = stats[4];
        willPower = stats[5];
        focus = stats[6];
        marksmanship = stats[7];
        magicResistance = stats[8];
    }

    /**
     * Increases this enemy's level by one.
     */
    public final void levelUp() {
        setLevel(getLevel() + 1);
    }

    /**
     * Gets the level of this enemy.
     *
     * @return The level of this enemy.
     */
    public final int getLevel() {
        return level;
    }

    @Override
    public String toString() {

        return "Enemy.java: [\n" + " name: " + spriteName + "\n level: " + level + "\n" + "[\n" + " attack: " + attack +
                "\n defense: " + defense + "\n speed: " + speed + "\n vigor: " + vigor + "\n stamina: " + stamina +
                "\n willPower: " + willPower + "\n focus: " + focus + "\n marksmanship: " + marksmanship +
                "\n magicResistance: " + magicResistance + "\n]\n]";
    }

    public interface AIInterface {
        public void onUpdate(Enemy enemy);
    }

}