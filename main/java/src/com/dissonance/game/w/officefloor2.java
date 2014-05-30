package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.office.OpenWindow;

public class officefloor2 extends DemoLevelWorldLoader {

    public static BlueGuard var1;
    public static BlueGuard var2;
    public static BlueGuard var3;
    public static BlueGuard var4;
    public static BlueGuard var5;
    public static BlueGuard var6;
    public static BlueGuard var7;

    public static BlueGuard[] meleeGuard = new BlueGuard[7];
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        for (int i = 0; i < meleeGuard.length; i++) {
            meleeGuard[i] = new BlueGuard();
        }

        meleeGuard[0].setX(28 * 16);
        meleeGuard[0].setY(28 * 16);
        w.loadAndAdd(meleeGuard[0]);

        meleeGuard[1].setX(36 * 16);
        meleeGuard[1].setY(28 * 16);
        w.loadAndAdd(meleeGuard[1]);

        meleeGuard[2].setX(31 * 16);
        meleeGuard[2].setY(68 * 16);
        w.loadAndAdd(meleeGuard[2]);

        meleeGuard[3].setX(57 * 16);
        meleeGuard[3].setY(28 * 16);
        w.loadAndAdd(meleeGuard[3]);

        meleeGuard[4].setX(44 * 16);
        meleeGuard[4].setY(86 * 16);
        w.loadAndAdd(meleeGuard[4]);

        meleeGuard[5].setX(52 * 16);
        meleeGuard[5].setY(28 * 16);
        w.loadAndAdd(meleeGuard[5]);

        meleeGuard[6].setX(56 * 16);
        meleeGuard[6].setY(10 * 16);
        w.loadAndAdd(meleeGuard[6]);


    }
    public void onDisplay(World w) {

        super.onDisplay(w);

        farrand.setVisible(false);
        jeremiah.setVisible(false);

        OpenWindow window = new OpenWindow();
        window.setX(56.5f * 16f);
        window.setY(3.5f * 16f);
        w.loadAndAdd(window);



    }

    @Override
    public void onRespawn(World w) {
        super.onRespawn(w);

        farrand.setUsePhysics(false);
        jeremiah.setUsePhysics(false);
        farrand.setX(18f * 16f);
        farrand.setY(8f * 16f);

        jeremiah.setX(21f * 16f);
        jeremiah.setY(8f * 16f);

        farrand.setUsePhysics(true);
        jeremiah.setUsePhysics(true);

        farrand.setVisible(false);
        jeremiah.setVisible(false);

        for (int i = 0; i < meleeGuard.length; i++) {
            meleeGuard[i] = new BlueGuard();

        }

        meleeGuard[0].setX(28 * 16);
        meleeGuard[0].setY(28 * 16);
        w.loadAndAdd(meleeGuard[0]);

        meleeGuard[1].setX(36 * 16);
        meleeGuard[1].setY(26 * 16);
        w.loadAndAdd(meleeGuard[1]);

        meleeGuard[2].setX(31 * 16);
        meleeGuard[2].setY(68 * 16);
        w.loadAndAdd(meleeGuard[2]);

        meleeGuard[3].setX(57 * 16);
        meleeGuard[3].setY(27 * 16);
        w.loadAndAdd(meleeGuard[3]);

        meleeGuard[4].setX(44 * 16);
        meleeGuard[4].setY(86 * 16);
        w.loadAndAdd(meleeGuard[4]);

        meleeGuard[5].setX(52 * 16);
        meleeGuard[5].setY(27 * 16);
        w.loadAndAdd(meleeGuard[5]);

        meleeGuard[6].setX(56 * 16);
        meleeGuard[6].setY(10 * 16);
        w.loadAndAdd(meleeGuard[6]);
    }
}
