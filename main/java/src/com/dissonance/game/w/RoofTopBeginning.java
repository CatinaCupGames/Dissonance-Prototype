/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/16 13:58:43
=====================================
*/

package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.BlueGuard;
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

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);

        jeremiah.setX(100f);
        jeremiah.setY(210f);
        jeremiah.setVisible(false);
    
        var2 = new BlueGuard();
        w.loadAndAdd(var2);
        var2.setX(295.75f);
        var2.setY(66.45f);
    
        var3 = new BlueGuard();
        w.loadAndAdd(var3);
        var3.setX(45.25f);
        var3.setY(33.85f);
    
        var4 = new BlueGuard();
        w.loadAndAdd(var4);
        var4.setX(180.4f);
        var4.setY(29.849995f);

        dec1 = new RoofAC();
        w.loadAndAdd(dec1);
        dec1.setX(18*16);
        dec1.setY(6*16);

        dec2 = new RoofFan();
        w.loadAndAdd(dec2);
        dec2.setX(18*16);
        dec2.setY(8*16);


        dec3 = new RoofVent();
        w.loadAndAdd(dec3);
        dec3.setX(14*16);
        dec3.setY(8*16);

        dec4 = new Skylight();
        w.loadAndAdd(dec4);
        dec4.setX(60*16);
        dec4.setY(26*16);
        dec4.setLayer(0);

        dec5 = new RoofAC();
        w.loadAndAdd(dec5);
        dec5.setX(72*16);
        dec5.setY(31*16);

        dec6 = new RoofFan();
        w.loadAndAdd(dec6);
        dec6.setX(72*16);
        dec6.setY(33*16);

        dec7 = new RoofVent();
        w.loadAndAdd(dec7);
        dec7.setX(68*16);
        dec7.setY(33*16);


        dec8 = new RoofEntry();
        w.loadAndAdd(dec8);
        dec8.setX(78*16);
        dec8.setY(22*16);




        dec9 = new Skylight();
        w.loadAndAdd(dec9);
        dec9.setX(80*16);
        dec9.setY(26*16);
        w.setWorldBrightness(0.7f);
        dec9.setLayer(0);



    }

    @Override
    public void onDisplay(World w){
        super.onDisplay(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);

        jeremiah.setX(100f);
        jeremiah.setY(210f);
        jeremiah.setVisible(false);

        dec2.setWidth(64);
        dec2.setHeight(32);

        dec1.setWidth(172);
        dec1.setHeight(64);

        dec4.setWidth(256);
        dec4.setHeight(96);

        dec6.setWidth(64);
        dec6.setHeight(32);

        dec5.setWidth(172);
        dec5.setHeight(64);

        dec9.setWidth(256);
        dec9.setHeight(96);


    }
}
