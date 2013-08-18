package com.tog.framework.system;

import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.game.sprites.impl.TestPlayer;
import com.tog.framework.game.world.World;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.ticker.Ticker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Game {
    public static int GAME_WIDTH = 1280, GAME_HEIGHT = 720;
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

    public static void main(String[] args) {
        System.out.println("Using libs folder " + System.getProperty("org.lwjgl.librarypath"));
        System.out.println("Starting Ticker");
        TICKER.startTick();
        World w = new World();
        w.init();
        try {
            w.load("test_world");
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * This is a stress test for sprite sorting.
         * In this test, x amount of TestPlayer objects are created and drawn on the screen.
         * Every 20 seconds, the player you play as is changed.
         *
         * It seems the current sorting method is perfectly fine. Feel free to play
         * around with the STRESS_COUNT value.
         */
        final int STRESS_COUNT = 10;
        final Random random = new Random();
        List<TestPlayer> testPlayers = new ArrayList<TestPlayer>();
        for (int i = 0; i < STRESS_COUNT; i++) {
            TestPlayer p = new TestPlayer();
            w.loadAnimatedTextureForSprite(p);
            w.addSprite(p);
            p.setX(random.nextInt(300));
            p.setY(random.nextInt(300));
            testPlayers.add(p);
        }

        w.invalidateList();
        for (TestPlayer testPlayer : testPlayers) {
            testPlayer.select();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    public static class TestSprite extends AnimatedSprite {

        @Override
        public String getSpriteName() {
            return "sprite_test";
        }
    }
}
