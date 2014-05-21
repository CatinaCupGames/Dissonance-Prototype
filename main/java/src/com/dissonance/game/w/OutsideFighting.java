package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.StreetLamp;

import java.awt.*;

public class OutsideFighting extends DemoLevelWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(22*16);
        farrand.setY(300*16);

        w.setWorldBrightness(0.96f);
        //DIS CUTOFF BULLSHIIIEEET
        //PLZ KILL ME

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(26*16);
                lamp.setY(y * 16);
                lamp.setLayer(11);
                lamp.setCutOffMargin(30f);
                w.createLight(26* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(26* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
            }else{
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(35*16);
                lamp.setY(y * 16);
                lamp.setLayer(11);
                lamp.setCutOffMargin(30f);
                w.createLight(35* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(35* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
            }
        }

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(121*16);
                lamp.setY(y * 16);
                lamp.setLayer(11);
                //lamp.setCutOffMargin(30f);
                w.createLight(121* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(121* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
            }else{
                StreetLamp lamp = new StreetLamp();
                w.loadAndAdd(lamp);
                lamp.setX(130*16);
                lamp.setY(y * 16);
                lamp.setLayer(11);
                //lamp.setCutOffMargin(33f);
                w.createLight(130* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(130* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
            }
        }
    }
}
