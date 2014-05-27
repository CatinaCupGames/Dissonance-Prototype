package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.RoofLight;
import com.dissonance.game.sprites.office.Window;


public class RooftopMid extends DemoLevelWorldLoader {
    public static RoofLight[] lights = new RoofLight[16];
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        Window window = new Window();
        window.setX(60.5f * 16f);
        window.setY(62.5f * 16f);
        window.setAnimation("breaking");
        window.setFrame(2);
        window.pauseAnimation();
        w.loadAndAdd(window);

        for(int i = 0; i < lights.length; i++){
            lights[i] = new RoofLight();
            w.loadAndAdd(lights[i]);
        }

        lights[0].setX(22*16);
        lights[0].setY(22*16);

        lights[1].setX(26*16);
        lights[1].setY(13*16);

        lights[2].setX(64*16);
        lights[2].setY(13*16);

        lights[3].setX(26*16);
        lights[3].setY(35*16);

        lights[4].setX(10*16);
        lights[4].setY(42*16);

        lights[5].setX(37*16);
        lights[5].setY(42*16);

        lights[6].setX(10*16);
        lights[6].setY(86*16);

        lights[7].setX(37*16);
        lights[7].setY(86*16);

        lights[8].setX(21*16);
        lights[8].setY(93*16);

        lights[9].setX(64*16);
        lights[9].setY(93*16);

        lights[10].setX(21*16);
        lights[10].setY(115*16);

        lights[11].setX(64*16);
        lights[11].setY(115*16);

        lights[12].setX(44*16);
        lights[12].setY(86*16);

        lights[13].setX(83*16);
        lights[13].setY(86*16);

        lights[14].setX(83*16);
        lights[14].setY(66*16);

        lights[15].setX(44*16);
        lights[15].setY(66*16);





    }
}
