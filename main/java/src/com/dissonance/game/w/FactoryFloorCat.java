package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class FactoryFloorCat extends DemoLevelWorldLoader {

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        farrand.setX(52 * 16f);
        farrand.setY(175 * 16f);
        farrand.setLayer(2);
    }
}
