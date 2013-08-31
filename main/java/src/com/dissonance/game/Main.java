package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.system.ticker.Ticker;
import com.dissonance.game.quests.TestQuest;

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
        //SteamProxy proxy = new SteamProxy();
        //proxy.initSteamAPI();
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
