/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/06 18:38:59
=====================================
*/
package com.dissonance.game.w;


import com.dissonance.framework.game.world.*;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.sprites.*;
import com.dissonance.game.sprites.office.*;


public class WaldomarsMeetingRoom implements WorldLoader {
    public static Waldomar waldomar;
    public static Jeremiah jeremiah;
    public static Farrand farrand;
    
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

farrand = new Farrand();
w.loadAndAdd(farrand);        
farrand.deselect();
        farrand.setX(548.3502f);
        farrand.setY(128.44998f);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(603.4502f);
        jeremiah.setY(129.25f);

        guard1 = new Wyatt();
        w.loadAndAdd(guard1);
        guard1.setX(509.69965f);
        guard1.setY(128.24994f);

        guard2 = new Wyatt();
        w.loadAndAdd(guard2);
        guard2.setX(529.60004f);
        guard2.setY(128.45f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard3);
        guard3.setX(566.4501f);
        guard3.setY(128.75002f);

        guard4 = new Wyatt();
        w.loadAndAdd(guard4);
        guard4.setX(584.8502f);
        guard4.setY(128.95f);

        guard5 = new Wyatt();
        w.loadAndAdd(guard5);
        guard5.setX(622.5496f);
        guard5.setY(129.9f);

        w.setWorldBrightness(0.4f);
        w.createLight(15f * 16, 2f * 16, 1.8f, 0.7f);
        w.createLight(0f, 0f, 0.5f, 0.1f);
    }

    @Override
    public void onDisplay(World w){ }
}
