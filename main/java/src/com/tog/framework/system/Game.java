package com.tog.framework.system;

import java.io.File;

public class Game {
    public static int GAME_WIDTH = 1280, GAME_HEIGHT = 720;
    static {
        String lwjgl_folder = "libs" + File.separator + "lwjgl_native" + File.separator;
        final String OS = System.getProperty("os.name");
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
    }
}
