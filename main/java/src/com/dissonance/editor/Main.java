package com.dissonance.editor;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.texture.TextureLoader;

import java.io.File;
import java.util.Locale;

public class Main {
    static {
        String lwjgl_folder = "libs" + File.separator + "lwjgl_native" + File.separator;
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        TextureLoader.setFastRedraw(false);
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
        GameService.loadEssentials(args);
        System.out.println("Starting MainQuest");
        GameService.beginQuest(new MainQuest());
    }
}
