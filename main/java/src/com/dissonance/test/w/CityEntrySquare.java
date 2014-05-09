/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/07 11:31:09
=====================================
*/
package com.dissonance.test.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.Wyatt;
import com.dissonance.game.w.GameWorldLoader;

public class CityEntrySquare extends GameWorldLoader {
    public static Wyatt guard5;
    public static Wyatt guard4;
    public static Jeremiah jeremiah;
    public static Wyatt guard1;
    public static Wyatt guard2;
    public static Wyatt guard3;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(845.60016f);
        farrand.setY(785.5205f);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(859.05994f);
        jeremiah.setY(760.96906f);
        jeremiah.joinParty(farrand);

        guard1 = new Wyatt();
        w.loadAndAdd(guard1);
        guard1.setX(764f);
        guard1.setY(725f);

        guard2 = new Wyatt();
        w.loadAndAdd(guard2);
        guard2.setX(764f);
        guard2.setY(760f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard3);
        guard3.setX(764f);
        guard3.setY(795f);
    
        guard4 = new Wyatt();
        w.loadAndAdd(guard4);
        guard4.setX(764f);
        guard4.setY(830f);
    
        guard5 = new Wyatt();
        w.loadAndAdd(guard5);
        guard5.setX(764f);
        guard5.setY(865f);
    }
}
