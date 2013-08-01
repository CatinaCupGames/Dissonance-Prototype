package com.tog.framework.system;

import com.tog.framework.game.sprites.impl.AnimatedSprite;
import com.tog.framework.game.world.World;
import com.tog.framework.render.Camera;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.ticker.Ticker;
import org.lwjgl.util.vector.Vector2f;

import java.io.File;
import java.util.Locale;

public class Game {
    public static int GAME_WIDTH = 1280, GAME_HEIGHT = 720;
    public static TestSprite TEST = new TestSprite();
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
    }

    public static void main(String[] args) {
        System.out.println("Using libs folder " + System.getProperty("org.lwjgl.librarypath"));
        System.out.println("Starting Ticker");
        TICKER.startTick();
        World w = new World();
        w.init();
        try {
            w.load("test");
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        w.loadAnimatedTextureForSprite(TEST);
        w.addSprite(TEST);
        TEST.setX(75);
        TEST.setY(50);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Camera.easeMovement(Camera.translateToCamera(TEST.getVector()), 2500);

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
