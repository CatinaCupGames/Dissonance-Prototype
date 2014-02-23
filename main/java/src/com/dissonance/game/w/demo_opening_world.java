package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.RenderService;

public class demo_opening_world extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);
    }

    @Override
    public void onDisplay(World w) {
        farrand.freeze();
        farrand.setY(40 * 16);
        farrand.setX(16);
    }
}
