package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.StreetLamp;

import java.awt.*;
import java.util.ArrayList;

public class OutsideFighting extends DemoLevelWorldLoader {
    int pleaseKillMe = 0;
    int pleaseKillMeAgain = 0;

    public static ArrayList<StreetLamp> lamps1 = new ArrayList<StreetLamp>();
    public static ArrayList<StreetLamp> lamps2 = new ArrayList<StreetLamp>();


    @Override
    public void onLoad(World w) {
        super.onLoad(w);



        w.setWorldBrightness(0.96f);

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                lamps1.add(new StreetLamp());
                w.loadAndAdd(lamps1.get(pleaseKillMe));
                lamps1.get(pleaseKillMe).setX(26*16);
                lamps1.get(pleaseKillMe).setY(y * 16);
                lamps1.get(pleaseKillMe).setLayer(1);
                w.createLight(26* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(26* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
                pleaseKillMe ++;
            }else{
                lamps1.add(new StreetLamp());
                w.loadAndAdd(lamps1.get(pleaseKillMe));
                lamps1.get(pleaseKillMe).setX(35 * 16);
                lamps1.get(pleaseKillMe).setY(y * 16);
                lamps1.get(pleaseKillMe).setLayer(1);

                w.createLight(35* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(35* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
                pleaseKillMe ++;
            }
        }

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                lamps2.add(new StreetLamp());
                w.loadAndAdd(lamps2.get(pleaseKillMeAgain));
                lamps2.get(pleaseKillMeAgain).setX(121 * 16);
                lamps2.get(pleaseKillMeAgain).setY(y * 16);
                lamps2.get(pleaseKillMeAgain).setLayer(1);
                w.createLight(121* 16, (y - 3.5f)* 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(121* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
                pleaseKillMeAgain ++;
            }else{
                lamps2.add(new StreetLamp());
                w.loadAndAdd(lamps2.get(pleaseKillMeAgain));
                lamps2.get(pleaseKillMeAgain).setX(130 * 16);
                lamps2.get(pleaseKillMeAgain).setY(y * 16);
                lamps2.get(pleaseKillMeAgain).setLayer(1);
                w.createLight(130 * 16, (y - 3.5f) * 16, 1.2f, 0.03f, Color.YELLOW);
                w.createLight(130* 16, (y - 3.5f)* 16, 1.4f, 0.73f);
                pleaseKillMeAgain ++;
            }
        }


    }
    @Override
    public void onDisplay(World w){
        super.onDisplay(w);
        farrand.setX(22*16);
        farrand.setY(300*16);

        for(int i = 0; i <= 23; i++){
            lamps1.get(i).setCutOffMargin(22f);
            lamps2.get(i).setCutOffMargin(22f);
        }

    }
}
