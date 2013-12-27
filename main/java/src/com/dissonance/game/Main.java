package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.ticker.Ticker;
import com.dissonance.game.quests.TestQuest;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static String DID;
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
        System.setProperty("java.library.path", System.getProperty("org.lwjgl.librarypath"));
    }

    public static void main(String[] args) throws Exception {
        GameService.args = args;
        System.out.println("Using libs folder " + System.getProperty("org.lwjgl.librarypath"));
        System.out.println("Loading Input config");
        InputKeys.initializeConfig();
        System.out.println("Loading game dialog");
        DialogFactory.loadDialog();
        System.out.println("Loading sounds");
        Sound.loadAllSounds();
        //dialogMenu();
        System.out.println("Starting TestQuest");
        GameService.beginQuest(new TestQuest());
    }

    private static void dialogMenu() {
        System.out.print("Please type in test dialog file: ");
        File fileObj;
        Scanner scanner;
        do {
            scanner = new Scanner(System.in);
            String file = scanner.nextLine();
            fileObj = new File(file);
            if (fileObj.exists() && !fileObj.isDirectory())
                break;
            System.out.println("Invalid dialog file!");
        } while (true);
        System.out.println("Loading dialog file \"" + fileObj.getName() + "\"");
        boolean success = DialogFactory.loadDialog(fileObj);
        if (!success) {
            System.out.println("Error loading dialog!");
            return;
        }
        System.out.print("Please type in the dialog ID to test: ");
        DID = scanner.nextLine();
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
