package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class OutsideFighting extends DemoLevelWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(166f*16f);
        farrand.setY(376f*16f);
    }
}
