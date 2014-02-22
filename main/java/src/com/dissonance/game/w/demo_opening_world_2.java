package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;

public class demo_opening_world_2 implements WorldLoader {

    @Override
    public void onLoad(World w) {
        w.createLight(0f, 0f, 1f, 0.01f);
        w.setWorldBrightness(0.6f);
    }

    @Override
    public void onDisplay(World world) { }
}
