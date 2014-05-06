/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/06 11:34:55
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.sprites.*;
import com.dissonance.game.sprites.office.*;


public class WaldomarsMeetingRoom extends GameWorldLoader {
    public static Waldomar waldomar;
    public static Jeremiah jeremiah;
    
    public static WaldomarDesk var8;
    public static Sofa var7;
    public static Sofa var6;
    public static Sofa var5;
    public static Bookshelf1 var1;
    public static Bookshelf1 var2;
    public static Bookshelf2 var4;
    public static BlackCover var9;

    public static Wyatt guard1;
    public static Wyatt guard2;
    public static Wyatt guard3;
    public static Wyatt guard4;
    public static Wyatt guard5;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);


        var1 = new Bookshelf1();
        w.loadAndAdd(var1);
        var1.setX(220.44997f);
        var1.setY(60f);

        var2 = new Bookshelf1();
        w.loadAndAdd(var2);
        var2.setX(323.49985f);
        var2.setY(60.0f);

        var4 = new Bookshelf2();
        w.loadAndAdd(var4);
        var4.setX(271.85007f);
        var4.setY(60.0f);
    
        var5 = new Sofa();
        w.loadAndAdd(var5);
        var5.setX(374.40015f);
        var5.setY(69.350006f);
    
        var6 = new Sofa();
        w.loadAndAdd(var6);
        var6.setX(396.70007f);
        var6.setY(69.350006f);
    
        var7 = new Sofa();
        w.loadAndAdd(var7);
        var7.setX(418.80014f);
        var7.setY(69.350006f);
    
        var8 = new WaldomarDesk();
        w.loadAndAdd(var8);
        var8.setX(113.5f);
        var8.setY(202.8f);

        var9 = new BlackCover();
        w.loadAndAdd(var9);
        var9.setX(600f);
        var9.setY(150f);
        var9.setLayer(3);
    
        waldomar = new Waldomar();
        w.loadAndAdd(waldomar);
        waldomar.setX(102.95f);
        waldomar.setY(203.85004f);

        farrand.deselect();
        farrand.setX(453.85f);
        farrand.setY(120.95f);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(503f);
        jeremiah.setY(150f);

        guard1 = new Wyatt();
        w.loadAndAdd(guard1);
        guard1.setX(503f);
        guard1.setY(150f);

        guard2 = new Wyatt();
        w.loadAndAdd(guard1);
        guard2.setX(503f);
        guard2.setY(150f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard1);
        guard3.setX(503f);
        guard3.setY(150f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard1);
        guard3.setX(503f);
        guard3.setY(150f);

        guard4 = new Wyatt();
        w.loadAndAdd(guard1);
        guard4.setX(503f);
        guard4.setY(150f);

        guard5 = new Wyatt();
        w.loadAndAdd(guard1);
        guard5.setX(503f);
        guard5.setY(150f);
    }

    @Override
    public void onDisplay(World w) {
        Sound.playSound("waldobuilding");
    }
}
