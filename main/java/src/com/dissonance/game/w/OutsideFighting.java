package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.Billboard;
import com.dissonance.game.sprites.StreetLamp;

import java.awt.*;
import java.util.ArrayList;

public class OutsideFighting extends DemoLevelWorldLoader {
    int pleaseKillMe = 0;
    int pleaseKillMeAgain = 0;

    public static ArrayList<StreetLamp> lamps1 = new ArrayList<StreetLamp>();
    public static ArrayList<StreetLamp> lamps2 = new ArrayList<StreetLamp>();

    public static Billboard billboard1;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        if (GameSettings.Graphics.qualityLights) {
            w.setWorldBrightness(0.96f);
            w.createLight(70*16, 301*16, 1.6f, 0.34f);
            w.createLight(64*16, 301*16, 1.6f, 0.34f);
        } else {
            w.setWorldBrightness(0.7f);
            w.createLight(-200, -200, 0.4f, 0.1f); //Lights for brightness
            w.createLight(-200, -200, 0.4f, 0.1f); //Lights for brightness
        }

        for(int y = 350; y>= 240; y -= 10){
            if((y / 10) % 2 == 0){
                lamps1.add(new StreetLamp());
                w.loadAndAdd(lamps1.get(pleaseKillMe));
                lamps1.get(pleaseKillMe).setX(26*16);
                lamps1.get(pleaseKillMe).setY(y * 16);
                lamps1.get(pleaseKillMe).setLayer(1);
                createLight(w, 26 * 16, (y - 3.5f) * 16, 1.2f, 0.03f, 512 * 0.03f, 0.03f, Color.YELLOW);
                createLight(w, 26 * 16, (y - 3.5f) * 16, 1.4f, 0.73f, 256f,  0.2f);
                pleaseKillMe ++;
            }else{
                lamps1.add(new StreetLamp());
                w.loadAndAdd(lamps1.get(pleaseKillMe));
                lamps1.get(pleaseKillMe).setX(35 * 16);
                lamps1.get(pleaseKillMe).setY(y * 16);
                lamps1.get(pleaseKillMe).setLayer(1);

                createLight(w, 35 * 16, (y - 3.5f) * 16, 1.2f, 0.03f, 512 * 0.03f, 0.03f, Color.YELLOW);
                createLight(w, 35 * 16, (y - 3.5f) * 16, 1.4f, 0.73f, 256f,  0.2f);
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
                createLight(w, 121 * 16, (y - 3.5f) * 16, 1.2f, 0.03f, 512 * 0.03f, 0.03f, Color.YELLOW);
                createLight(w, 121 * 16, (y - 3.5f) * 16, 1.4f, 0.73f, 256f,  0.2f);
                pleaseKillMeAgain ++;
            }else {
                lamps2.add(new StreetLamp());
                w.loadAndAdd(lamps2.get(pleaseKillMeAgain));
                lamps2.get(pleaseKillMeAgain).setX(130 * 16);
                lamps2.get(pleaseKillMeAgain).setY(y * 16);
                lamps2.get(pleaseKillMeAgain).setLayer(1);
                createLight(w, 130 * 16, (y - 3.5f) * 16, 1.2f, 0.03f, 512 * 0.03f, 0.03f, Color.YELLOW);
                createLight(w, 130 * 16, (y - 3.5f) * 16, 1.4f, 0.73f, 256f,  0.2f);
                pleaseKillMeAgain ++;
            }
        }
        /*w.createLight(70*16, 301*16, 1.6f, 0.34f);
        w.createLight(64*16, 301*16, 1.6f, 0.34f);*/

        //82, 307
        billboard1 = new Billboard();
        w.loadAndAdd(billboard1);
        billboard1.setX(82*16);
        billboard1.setY(307*16);
        billboard1.setAnimation("billboard1");

        w.createLight(55*16, 222*16, 1.5f, 0.5f);
        w.createLight(62*16, 222*16, 1.5f, 0.5f);


    }

    @Override
    public void onRespawn(World w) {
        super.onRespawn(w);

        farrand.setX(27f * 16f);
        farrand.setY(240f*16f);

        jeremiah.setX(24f*16f);
        jeremiah.setY(240f*16f);
    }

    @Override
    public void onDisplay(World w){
        super.onDisplay(w);

        farrand.setX(27f * 16f);
        farrand.setY(240f*16f);

        jeremiah.setX(24f*16f);
        jeremiah.setY(240f*16f);

        for(StreetLamp lamp : lamps1) {
            lamp.setCutOffMargin(22f);
        }

        for (StreetLamp lamp : lamps2) {
            lamp.setCutOffMargin(22f);
        }
    }
}
