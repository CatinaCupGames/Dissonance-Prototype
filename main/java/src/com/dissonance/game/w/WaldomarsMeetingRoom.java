/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/14 11:40:16
=====================================
*/

/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/14 11:22:25
=====================================
*/

/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/14 10:47:53
=====================================
*/

/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/07 11:55:31
=====================================
*/
package com.dissonance.game.w;


import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.shader.impl.Light;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.sprites.*;
import com.dissonance.game.sprites.office.*;
import com.dissonance.game.sprites.office.Window;

import java.awt.*;


public class WaldomarsMeetingRoom implements WorldLoader {
  
    public static waldomarchair var32;
    public static waldomarchair var31;
 
    public static Trashbin var29;
    public static Trashbin var28;
  
    public static CoffeeCup var26;
    public static Sofa var25;
    public static CoffeeTable var24;
    
    public static PottedPlant var23;
    public static PottedPlant var22;
    public static PottedPlant var21;
    public static PottedPlant var20;
    
    public static Window var19;
    public static DeskRadio var17;
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

    public static BlueGuard guard1;
    public static BlueGuard guard2;
    public static BlueGuard guard3;
    public static BlueGuard guard4;
    public static BlueGuard guard5;

    public static Light l;

    public static FireBall fireball;

    @Override
    public void onLoad(World w) {

        //es fucked up, senor
        //feex it

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
        farrand.setX(548.3502f);
        farrand.setY(128.44998f);
        farrand.setUsePhysics(false);
        farrand.setMovementType(MovementType.WALKING);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(603.4502f);
        jeremiah.setY(129.25f);
        jeremiah.setUsePhysics(false);
        jeremiah.setMovementType(MovementType.WALKING);

        guard1 = new BlueGuard();
        w.loadAndAdd(guard1);
        guard1.setX(31*16);
        guard1.setY(9*16);
        guard1.setUsePhysics(false);
        guard1.setMovementType(MovementType.WALKING);

        guard2 = new BlueGuard();
        w.loadAndAdd(guard2);
        guard2.setX(529.60004f);
        guard2.setY(128.45f);
        guard2.setUsePhysics(false);
        guard2.setMovementType(MovementType.WALKING);

        guard3 = new BlueGuard();
        w.loadAndAdd(guard3);
        guard3.setX(566.4501f);
        guard3.setY(9*16);
        guard3.setUsePhysics(false);
        guard3.setMovementType(MovementType.WALKING);

        guard4 = new BlueGuard();
        w.loadAndAdd(guard4);
        guard4.setX(584.8502f);
        guard4.setY(9*16);
        guard4.setUsePhysics(false);
        guard4.setMovementType(MovementType.WALKING);

        guard5 = new BlueGuard();
        w.loadAndAdd(guard5);
        guard5.setX(622.5496f);
        guard5.setY(9*16);
        guard5.setUsePhysics(false);
        guard5.setMovementType(MovementType.WALKING);

        var17 = new DeskRadio();
        w.loadAndAdd(var17);
        var17.setX(117.45002f);
        var17.setY(185.20001f);
        var17.setLayer(2);

        var19 = new Window();
        w.loadAndAdd(var19);
        var19.setX(104f);
        var19.setY(40f);
    
    
        var20 = new PottedPlant();
        w.loadAndAdd(var20);
        var20.setX(447.75015f);
        var20.setY(260.15002f);
    
        var21 = new PottedPlant();
        w.loadAndAdd(var21);
        var21.setX(11.15f);
        var21.setY(70.34999f);
    
        var22 = new PottedPlant();
        w.loadAndAdd(var22);
        var22.setX(447.0f);
        var22.setY(72.04f);
    
        var23 = new PottedPlant();
        w.loadAndAdd(var23);
        var23.setX(12.0f);
        var23.setY(260.75003f);
    
        
    
        var24 = new CoffeeTable();
        w.loadAndAdd(var24);
        var24.setX(399.74999f);
        var24.setY(254.15f);
    
        var25 = new Sofa();
        w.loadAndAdd(var25);
        var25.setX(400.7f);
        var25.setY(237.3f);
    
        var26 = new CoffeeCup();
        w.loadAndAdd(var26);
        var26.setX(400.7f);
        var26.setY(254.15f);
        
    
        var28 = new Trashbin();
        w.loadAndAdd(var28);
        var28.setX(135.25f);
        var28.setY(211.7f);
    
        var29 = new Trashbin();
        w.loadAndAdd(var29);
        var29.setX(353.69998f);
        var29.setY(75.0f);
    
       
    
        var31 = new waldomarchair();
        w.loadAndAdd(var31);
        var31.setX(109.65f);
        var31.setY(222.15f);
    
        var32 = new waldomarchair();
        w.loadAndAdd(var32);
        var32.setX(400.7f);
        var32.setY(274.15f);

        fireball = new FireBall();
        w.loadAndAdd(fireball);
        fireball.setX(waldomar.getX());
        fireball.setY(waldomar.getY());
        fireball.setVisible(false);

        guard1.setHostile(false);
        guard2.setHostile(false);
        guard3.setHostile(false);
        guard4.setHostile(false);
        guard5.setHostile(false);

        guard1.setUsePhysics(false);
        guard2.setUsePhysics(false);
        guard3.setUsePhysics(false);
        guard4.setUsePhysics(false);
        guard5.setUsePhysics(false);
        farrand.setUsePhysics(false);
        jeremiah.setUsePhysics(false);

      w.setWorldBrightness(0.5f);
        w.createLight(15f * 16, 2f * 16, 1.4f, 0.7f);
        //w.createLight(0f, 0f, 0.1f, 0.1f);
        l = w.createLight(-200f, -200f, 0.01f, 0.01f, Color.RED);
    }

    @Override
    public void onDisplay(World w){ 
        var26.setHeight(12f);
        var26.setWidth(12f);
    }
}