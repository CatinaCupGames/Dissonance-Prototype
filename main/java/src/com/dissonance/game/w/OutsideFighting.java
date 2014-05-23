package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class OutsideFighting extends DemoLevelWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(56f*16f);
        farrand.setY(350f*16f);
    }
}
