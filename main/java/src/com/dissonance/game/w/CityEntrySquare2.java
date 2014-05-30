package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Admin;

public class CityEntrySquare2 extends DemoLevelWorldLoader {
    public static Admin admin1;
    public static Admin admin2;
    public static Admin admin3;
    @Override
    public void onLoad(World w){
        super.onLoad(w);

        admin1 = new Admin();
        w.loadAndAdd(admin1);
        admin1.setX(43*16);
        admin1.setY(56*16);

        admin2 = new Admin();
        w.loadAndAdd(admin2);
        admin2.setX(57*16);
        admin2.setY(56*16);

        admin3 = new Admin();
        w.loadAndAdd(admin3);
        admin3.setX(43*16);
        admin3.setY(56*16);
    }

    @Override
    public void onDisplay(World w){
        super.onDisplay(w);
    }


}