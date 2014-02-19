package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class demo_opening_world extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        GameWorldLoader.farrand.setY(40 * 16);
    }
}
