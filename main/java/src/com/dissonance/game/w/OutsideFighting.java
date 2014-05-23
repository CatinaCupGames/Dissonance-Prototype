package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class OutsideFighting extends DemoLevelWorldLoader {

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);
        farrand.setY(263f * 16f);
        farrand.setX(16f * 10f);
    }
}
