package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.menu.Banner;
import com.dissonance.game.sprites.menu.loading.Static;

public class MainMenu implements WorldLoader {
    @Override
    public void onLoad(World world) {


        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        world.loadAndAdd(s);
    }

    @Override
    public void onDisplay(World world) {

    }
}
