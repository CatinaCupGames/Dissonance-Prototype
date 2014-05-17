package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.Farrand;

public class OutsideFighting extends GameWorldLoader {
    public static Farrand farrand;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand = new Farrand();
        w.loadAndAdd(farrand);
        farrand.setX(97.9f);
        farrand.setY(204.34998f);
    }
}
