package com.tog.framework.system;

import com.tog.framework.game.world.World;
import com.tog.framework.render.RenderService;

import java.io.File;
import java.util.Locale;

public class Game {
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
        System.out.println("Starting RenderService.");
        Service s = ServiceManager.createService(RenderService.class);
        System.out.println("Passing dummy world.");
        World w = new World();
        s.provideData(w, RenderService.WORLD_DATA_TYPE);
        w.uwotm8();
    }
}
