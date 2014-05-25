package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
<<<<<<< HEAD
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.StreetLamp;
import com.dissonance.game.sprites.environment.BasicLight;

import java.awt.*;
import java.util.ArrayList;
=======
>>>>>>> cda4cb53d61761fd606bb5123ec1f4be20a41638

public class OutsideFighting extends DemoLevelWorldLoader {
    int pleaseKillMe = 0;
    int pleaseKillMeAgain = 0;

    public static ArrayList<StreetLamp> lamps1 = new ArrayList<StreetLamp>();
    public static ArrayList<StreetLamp> lamps2 = new ArrayList<StreetLamp>();

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        if (GameSettings.Graphics.qualityLights) {
            w.setWorldBrightness(0.96f);
        } else {
            w.setWorldBrightness(0.4f);
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
    }

    @Override
    public void onDisplay(World w){
        super.onDisplay(w);

        for(StreetLamp lamp : lamps1){
            lamp.setCutOffMargin(22f);
        }

        for (StreetLamp lamp : lamps2) {
            lamp.setCutOffMargin(22f);
        }
    }
}
