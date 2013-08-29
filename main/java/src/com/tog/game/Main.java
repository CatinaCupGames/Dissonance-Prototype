package com.tog.game;

import com.tog.framework.game.GameService;
import com.tog.framework.game.input.InputKeys;
import com.tog.framework.system.ticker.Ticker;
import com.tog.game.test.TestQuest;

import java.io.File;
import java.util.Locale;

public class Main {
    private static final Ticker TICKER = new Ticker();

    static {
        String lwjgl_folder = "libs" + File.separator + "lwjgl_native" + File.separator;
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if (OS.contains("win"))
            lwjgl_folder += "windows";
        else if (OS.contains("mac"))
            lwjgl_folder += "macosx";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            lwjgl_folder += "linux";
        else if (OS.contains("sunos"))
            lwjgl_folder += "solaris";
        System.setProperty("org.lwjgl.librarypath", new File(lwjgl_folder).getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Using libs folder " + System.getProperty("org.lwjgl.librarypath"));
        System.out.println("Starting Ticker");
        InputKeys.initializeConfig();

        TICKER.startTick();

        GameService.beginQuest(new TestQuest());
    }


    private static boolean started;
    public static void startGame() {
        if (started)
            return;
        started = true;
    }

    public static Ticker getSystemTicker() {
        return TICKER;
    }
}
