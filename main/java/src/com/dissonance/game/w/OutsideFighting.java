package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.StreetLamp;

public class OutsideFighting extends DemoLevelWorldLoader {
    public static StreetLamp l1;
    public static StreetLamp l2;
    public static StreetLamp l3;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);

        l1 = new StreetLamp();
        w.loadAndAdd(l1);
        l1.setX(35*16);
        l1.setY(350*16);

        l2 = new StreetLamp();
        w.loadAndAdd(l2);
        l2.setX(26*16);
        l2.setY(340*16);

        l3 = new StreetLamp();
        w.loadAndAdd(l3);
        l3.setX(35*16);

        l3.setY(330*16);
    }
}
