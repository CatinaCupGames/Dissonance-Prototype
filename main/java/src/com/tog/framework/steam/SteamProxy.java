package com.tog.framework.steam;

import java.io.File;
import java.util.Locale;

public class SteamProxy {
    static {
        String steam_libs = "libs" + File.separator + "steam_java" + File.separator;
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        final String ARCH = System.getProperty("os.arch");
        if (OS.contains("win"))
            steam_libs += "windows" + File.separator + (ARCH.contains("64") ? "x64" : "x86") + File.separator + "SteamProxy.dll";
        else if (OS.contains("mac"))
            steam_libs += "macosx";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            steam_libs += "linux";
        else if (OS.contains("sunos"))
            steam_libs += "solaris";
        System.setProperty("java.library.path", steam_libs);
        System.load(new File(steam_libs).getAbsolutePath());
    }


    private native boolean _initSteamAPI();

    private native boolean _closeSteamAPI();

    public void initSteamAPI() throws Exception {
        if (!_initSteamAPI())
            throw new Exception("Error starting Steam API");
    }

    public void disposeSteamAPI() {
        _closeSteamAPI();
    }
}
