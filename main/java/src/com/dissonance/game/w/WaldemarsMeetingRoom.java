/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/05 11:54:05
=====================================
*/
/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/05 11:37:47
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.office.*;

public class WaldemarsMeetingRoom extends GameWorldLoader {
    public static DeskChair var10;
    public static Sofa var9;
    public static Sofa var8;
    public static Sofa var7;
    public static PersonalDesk var6;
    public static Bookshelf1 var5;
    public static Bookshelf1 var4;
    public static Bookshelf1 var3;
    public static Bookshelf1 var2;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);
    
        var2 = new Bookshelf1();
        w.loadAndAdd(var2);
        var2.setX(184.0f);
        var2.setY(57.65f);
    
        var3 = new Bookshelf1();
        w.loadAndAdd(var3);
        var3.setX(235.04999f);
        var3.setY(57.65f);
    
        var4 = new Bookshelf1();
        w.loadAndAdd(var4);
        var4.setX(285.15f);
        var4.setY(57.65f);
    
        var5 = new Bookshelf1();
        w.loadAndAdd(var5);
        var5.setX(134.69998f);
        var5.setY(57.65f);
    
        var6 = new PersonalDesk();
        w.loadAndAdd(var6);
        var6.setX(132.35002f);
        var6.setY(203.8f);
    
        var7 = new Sofa();
        w.loadAndAdd(var7);
        var7.setX(52.949997f);
        var7.setY(72.0f);
    
        var8 = new Sofa();
        w.loadAndAdd(var8);
        var8.setX(77.35f);
        var8.setY(72.05f);
    
        var9 = new Sofa();
        w.loadAndAdd(var9);
        var9.setX(28.599997f);
        var9.setY(72.05f);
    
        var10 = new DeskChair();
        w.loadAndAdd(var10);
        var10.setX(103.850006f);
        var10.setY(184.1f);
    }
}
