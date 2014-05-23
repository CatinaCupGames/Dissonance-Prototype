package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;

/**
 * Created by AJ on 5/23/2014.
 */
public class OutsideFighting extends DemoLevelWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(56f*16f);
        farrand.setY(350f*16f);
    }
}
