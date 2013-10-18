package com.dissonance.framework.game.ai.astar;

public final class FastMath {
    private static final int BIG_ENOUGH_INT = 16 * 1024;
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT + 0.0000;
    private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5000;
    private static final double BIG_ENOUGH_CEIL = BIG_ENOUGH_INT + 0.9999;

    public static int fastFloor(float x) {
        return (int) (x + BIG_ENOUGH_FLOOR) - BIG_ENOUGH_INT;
    }

    public static int fastRound(float x) {
        return (int) (x + BIG_ENOUGH_ROUND) - BIG_ENOUGH_INT;
    }

    public static int fastCeil(float x) {
        return (int) (x + BIG_ENOUGH_CEIL) - BIG_ENOUGH_INT;
    }
}