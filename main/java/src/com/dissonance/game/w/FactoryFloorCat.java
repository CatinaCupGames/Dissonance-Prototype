package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

public class FactoryFloorCat extends DemoLevelWorldLoader {
    /*
    LAYER 2 = PLAYER
    LAYER 6 = CATWALK LAYER
     */

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        farrand.setX(52f * 16f);
        farrand.setY(175f * 16f);
        farrand.setLayer(2);

        jeremiah.setX(53f * 16f);
        jeremiah.setY(176f * 16f);
        jeremiah.setLayer(2);
        jeremiah.setVisible(false);
    }
}
