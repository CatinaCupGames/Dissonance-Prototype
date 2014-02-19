package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class demo_city_level1 extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(174 * 16);
        farrand.setY(74 * 16);
    }
}
