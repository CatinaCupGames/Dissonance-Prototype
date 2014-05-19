package com.dissonance.test.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.w.DemoLevelWorldLoader;

public class AICity extends DemoLevelWorldLoader {
    public static Farrand farrand;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand = new Farrand();
        w.loadAndAdd(farrand);
    }
}
