/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/16 13:58:43
=====================================
*/

package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.RoofLight;
import com.dissonance.game.sprites.roof.*;

public class RoofTopBeginning extends DemoLevelWorldLoader {
    public static BlueGuard var4;
    public static BlueGuard var3;
    public static BlueGuard var2;
    public static RoofAC dec1;
    public static RoofFan dec2;
    public static RoofVent dec3;
    public static Skylight dec4;
    public static RoofAC dec5;
    public static RoofFan dec6;
    public static RoofVent dec7;
    public static RoofEntry dec8;
    public static Skylight dec9;
    public static RoofLight r1;
    public static RoofLight r2;
    public static RoofLight r3;
    public static RoofLight r4;
    public static RoofLight r5;

    public static BlueGuard[] meleeGuard = new BlueGuard[3];

    public static Garbagechute exit;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);
        w.setWorldBrightness(0.8f);

        exit = new Garbagechute();
        exit.setX((88f * 16f) + 10);
        exit.setY(60f * 16f);
        exit.setLayer(100);
        w.loadAndAdd(exit);


        for(int i = 0; i < meleeGuard.length; i++){
            meleeGuard[i] = new BlueGuard();
            w.loadAndAdd(meleeGuard[i]);

        }

        meleeGuard[0].setX(72*16);
        meleeGuard[0].setY(21*16);

        meleeGuard[1].setX(71*16);
        meleeGuard[1].setY(28*16);

        meleeGuard[2].setX(64 * 16);
        meleeGuard[2].setY(20 * 16);

        r1 = new RoofLight();
        w.loadAndAdd(r1);
        r1.setX(32*16);
        r1.setY(20*16);
        w.createLight(32*16, 20*16, 1.4f, 0.5f);

        r2 = new RoofLight();
        w.loadAndAdd(r2);
        r2.setX(38*16);
        r2.setY(13*16);
        w.createLight(38*16, 13*16, 1.4f, 0.5f);

        r3 = new RoofLight();
        w.loadAndAdd(r3);
        r3.setX(38*16);
        r3.setY(40*16);
        w.createLight(38*16, 40*16, 1.4f, 0.5f);

        r4 = new RoofLight();
        w.loadAndAdd(r4);
        r4.setX(86*16);
        r4.setY(13*16);
        w.createLight(86*16, 13*16, 1.4f, 0.5f);

        r5 = new RoofLight();
        w.loadAndAdd(r5);
        r5.setX(86*16);
        r5.setY(40*16);
        w.createLight(86*16, 40*16, 1.4f, 0.5f);
    }

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        farrand.setUsePhysics(false);
        farrand.setX(4f * 16f);
        farrand.setY(14f * 16f);
        farrand.face(Direction.UP);
        farrand.freeze();
        farrand.setUsePhysics(true);

        jeremiah.setUsePhysics(false);
        jeremiah.setX(6f * 16f);
        jeremiah.setY(14f * 16f);
        jeremiah.face(Direction.UP);
        jeremiah.freeze();
        jeremiah.setUsePhysics(true);
    }

    @Override
    public void onRespawn(World w) {
        super.onRespawn(w);

        farrand.setX(4f * 16f);
        farrand.setY(14f * 16f);

        jeremiah.setX(6f * 16f);
        jeremiah.setY(14f * 16f);
        jeremiah.setBehavior(null);

        for(int i = 0; i < meleeGuard.length; i++){
            meleeGuard[i] = new BlueGuard();
            w.loadAndAdd(meleeGuard[i]);

        }

        meleeGuard[0].setX(72*16);
        meleeGuard[0].setY(21*16);

        meleeGuard[1].setX(71*16);
        meleeGuard[1].setY(28*16);

        meleeGuard[2].setX(64 * 16);
        meleeGuard[2].setY(20 * 16);
    }
}
