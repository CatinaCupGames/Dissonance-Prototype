package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.shader.impl.Light;

public class Zesilia_pan implements WorldLoader {
    @Override
    public void onLoad(World world) {
        world.setWorldBrightness(0.6f);

        world.createLight(0f, 0f, 1.1f, 1.f);
        world.createLight(0f, 0f, 1.1f, 1.f);
    }

    @Override
    public void onDisplay(World world) {

    }
}
