package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Jeremiah;

public class demo_city_level1 extends GameWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(174 * 16);
        farrand.setY(74 * 16);

        Jeremiah jeremiah = new Jeremiah();
        jeremiah.setX(2684);
        jeremiah.setY(1184);
        w.loadAndAdd(jeremiah);
        //jeremiah.select();
    }
}
