package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class OutsideFighting extends DemoLevelWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);
        w.loadAndAdd(farrand);
    }
}
