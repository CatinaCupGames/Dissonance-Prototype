/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/16 13:58:43
=====================================
*/

package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.*;

public class RoofTopBeginning extends GameWorldLoader {
    public static BlueGuard var4;
    public static BlueGuard var3;
    public static BlueGuard var2;
    public static Farrand farrand;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand = new Farrand();
        w.loadAndAdd(farrand);
        farrand.setX(97.9f);
        farrand.setY(204.34998f);
    
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
    }
}
