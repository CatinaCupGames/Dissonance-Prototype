/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/07 11:31:09
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.Wyatt;

public class CityEntrySquare extends GameWorldLoader {
    public static Wyatt guard5;
    public static Wyatt guard4;
    public static Wyatt guard1;
    public static Wyatt guard2;
    public static Wyatt guard3;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(985.60016f);
        farrand.setY(785.5205f);
        Camera.setPos(Camera.translateToCameraCenter(farrand.getVector(), farrand.getHeight()));

        jeremiah.setX(1019.05994f);
        jeremiah.setY(760.96906f);

        guard1 = new Wyatt();
        w.loadAndAdd(guard1);
        guard1.setX(924f);
        guard1.setY(725f);

        guard2 = new Wyatt();
        w.loadAndAdd(guard2);
        guard2.setX(924f);
        guard2.setY(760f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard3);
        guard3.setX(924f);
        guard3.setY(795f);
    
        guard4 = new Wyatt();
        w.loadAndAdd(guard4);
        guard4.setX(924f);
        guard4.setY(830f);
    
        guard5 = new Wyatt();
        w.loadAndAdd(guard5);
        guard5.setX(924f);
        guard5.setY(865f);

        w.setWorldBrightness(0.4f);
        w.createLight(0, 0, 0.1f, 0.1f);
    }
}
