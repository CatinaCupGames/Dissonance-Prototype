package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.StreetLamp;

import java.awt.*;

public class OutsideFighting extends DemoLevelWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(0);
        farrand.setY(0);

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(26*16);
                lamp.setY(y * 16);
                w.createLight(26* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
            }else{
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(35*16);
                lamp.setY(y * 16);
                w.createLight(35* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
            }
        }

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(121*16);
                lamp.setY(y * 16);
                w.createLight(121* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
            }else{
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(130*16);
                lamp.setY(y * 16);
                w.createLight(130* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
            }
        }
    }
}
