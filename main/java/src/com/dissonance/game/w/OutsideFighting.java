package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.BlueGuard;

public class OutsideFighting extends DemoLevelWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);/*

        var4.setX(201f*16f);
        var4.setY(201f*16f);*/

        w.setWorldBrightness(0.4f);
        w.createLight(0, 0, 0.1f, 0.1f);
    }

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        farrand.setX(22f*16f);
        farrand.setY(336f*16f);
    }
}
