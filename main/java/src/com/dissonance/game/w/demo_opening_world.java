package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class demo_opening_world extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);
    }

    @Override
    public void onDisplay(World w) {
        farrand.setY(40 * 16);
        farrand.setX(16);
    }
}
