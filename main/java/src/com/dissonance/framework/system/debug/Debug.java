package com.dissonance.framework.system.debug;

public class Debug {
    private static boolean debug;

    public static void debugMode(boolean debug) {
        Debug.debug = debug;
        System.err.println("Debug mode is " + (debug ? "on" : "off"));
    }

    public static boolean isDebugging() {
        return debug;
    }

    public static double getFreeMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

    public static double getPercentUsed() {
        return (double)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory();
    }
}
