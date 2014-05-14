package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.menu.Banner;

public class MainMenu implements WorldLoader {
    @Override
    public void onLoad(World world) {
        Banner banner = new Banner();
        world.loadAndAdd(banner);

        banner.setX((GameSettings.Display.window_width / 4f) + (332 / 4f));
        banner.setY((80 / 2f) + 40f);
    }

    @Override
    public void onDisplay(World world) {

    }
}
