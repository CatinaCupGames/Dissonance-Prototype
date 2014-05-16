package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Farrand;

public class RoofTopBeginning extends GameWorldLoader {
    public static Farrand farrand;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand = new Farrand();
        w.loadAndAdd(farrand);
    }
}
