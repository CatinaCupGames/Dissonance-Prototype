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

    public static Garbagechute exit;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(97.9f);
        farrand.setY(204.34998f);

        jeremiah.setX(100f);
        jeremiah.setY(210f);
        jeremiah.setVisible(false);

        exit = new Garbagechute();
        exit.setX((88f * 16f) + 10);
        exit.setY(60f * 16f);
        exit.setLayer(100);
        w.loadAndAdd(exit);



    }
}
