package com.dissonance.framework.game.sprites;

import java.util.Random;

public class Enemy {
    private static final byte STAT_COUNT = 9;
    private static Random random = new Random();

    public enum StatType {
        MAGIC, NON_MAGIC
    }

    /**
     * Generates random stats for this enemy. The generated array will contain the Attack, Defense, Speed, Vigor,
     * Stamina, Willpower, Focus, Marksmanship and Magic resistance stats respectively
     *
     * @param points The total number of stat points this enemy has.
     * @param type The stat type of this enemy.
     */
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
}