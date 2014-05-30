package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.RedGuard;
import com.dissonance.game.sprites.RoofLight;
import com.dissonance.game.sprites.office.OpenWindow;


public class RooftopMid extends DemoLevelWorldLoader {
    public static RoofLight[] lights = new RoofLight[16];
    private static final float BRIGHTNESS = 1.1f;
    private static final float RADIUS = 0.6f;

    public static RedGuard[] gunGuards = new RedGuard[3];

    public static BlueGuard[] meleeguards = new BlueGuard[5];
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        OpenWindow window = new OpenWindow();
        window.setX(60.5f * 16f);
        window.setY(62.5f * 16f);
        w.loadAndAdd(window);

        for(int i = 0; i < lights.length; i++){
            lights[i] = new RoofLight();
            w.loadAndAdd(lights[i]);
        }

        lights[0].setX(21*16);
        lights[0].setY(21*16);
        createLight(w, 21*16, 21*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[1].setX(27*16);
        lights[1].setY(14*16);
        createLight(w, 27*16, 14*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[2].setX(63*16);
        lights[2].setY(14*16);
        createLight(w, 64*16, 13*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[3].setX(27*16);
        lights[3].setY(34*16);
        createLight(w, 26*16, 35*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[4].setX(11*16);
        lights[4].setY(43*16);
        createLight(w, 10*16, 42*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[5].setX(36*16);
        lights[5].setY(43*16);
        createLight(w, 37*16, 42*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[6].setX(11*16);
        lights[6].setY(85*16);
        createLight(w, 10*16, 86*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[7].setX(36*16);
        lights[7].setY(85*16);
        createLight(w, 37*16, 86*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[8].setX(22*16);
        lights[8].setY(94*16);
        createLight(w, 21*16, 93*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[9].setX(63*16);
        lights[9].setY(94*16);
        createLight(w, 64*16, 93*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[10].setX(22*16);
        lights[10].setY(114*16);
        createLight(w, 21*16, 115*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[11].setX(63*16);
        lights[11].setY(114*16);
        createLight(w, 64*16, 115*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[12].setX(45*16);
        lights[12].setY(85*16);
        createLight(w, 44*16, 86*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[13].setX(82*16);
        lights[13].setY(85*16);
        createLight(w, 83*16, 86*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[14].setX(82*16);
        lights[14].setY(67*16);
        createLight(w, 83*16, 66*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        lights[15].setX(45*16);
        lights[15].setY(67*16);
        createLight(w, 44*16, 66*16, BRIGHTNESS, RADIUS, RADIUS, BRIGHTNESS);

        for(int i = 0; i < gunGuards.length; i ++){
            gunGuards[i] = new RedGuard();
        }

        gunGuards[0].setX(45*16);
        gunGuards[0].setY(15*16);
        w.loadAndAdd(gunGuards[0]);

        gunGuards[1].setX(16*16);
        gunGuards[1].setY(74*16);
        w.loadAndAdd(gunGuards[1]);

        gunGuards[2].setX(42*16);
        gunGuards[2].setY(101*16);
        w.loadAndAdd(gunGuards[2]);

        for(int i = 0; i < meleeguards.length; i++){
            meleeguards[i] = new BlueGuard();
        }

        meleeguards[0].setX(27*16);
        meleeguards[0].setY(24*16);
        w.loadAndAdd(meleeguards[0]);

        meleeguards[1].setX(36*16);
        meleeguards[1].setY(33*16);
        w.loadAndAdd(meleeguards[1]);

        meleeguards[2].setX(24*16);
        meleeguards[2].setY(44*16);
        w.loadAndAdd(meleeguards[2]);

        meleeguards[3].setX(32*16);
        meleeguards[3].setY(101*16);
        w.loadAndAdd(meleeguards[3]);

        meleeguards[4].setX(37*16);
        meleeguards[4].setY(101*16);
        w.loadAndAdd(meleeguards[4]);

    }

    @Override
    public void onRespawn(World w) {
        super.onRespawn(w);

        farrand.setX(8f * 16f);
        farrand.setY(7f * 16f);
        jeremiah.setX(7f * 16f);
        jeremiah.setY(2f * 16f);
    }
}
