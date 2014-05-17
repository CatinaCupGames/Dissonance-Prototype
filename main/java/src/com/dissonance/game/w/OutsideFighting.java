package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.Farrand;

public class OutsideFighting extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);
    }
}
