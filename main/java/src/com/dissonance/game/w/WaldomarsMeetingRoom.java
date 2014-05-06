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
    
    public static WaldomarDesk var8;
    public static Sofa var7;
    public static Sofa var6;
    public static Sofa var5;
    public static Bookshelf1 var1;
    public static Bookshelf1 var2;
    public static Bookshelf2 var4;
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
    
        waldomar = new Waldomar();
        w.loadAndAdd(waldomar);
        waldomar.setX(102.95f);
        waldomar.setY(203.85004f);

        farrand.deselect();
        farrand.setX(403.85f);
        farrand.setY(120.95f);

    }

    @Override
    public void onDisplay(World w) {
        Sound.playSound("waldobuilding");
    }
}
