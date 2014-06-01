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
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.sprites.BlueGuard;

public class CityEntrySquare extends DemoLevelWorldLoader {
    public static BlueGuard guard5;
    public static BlueGuard guard4;
    public static BlueGuard guard1;
    public static BlueGuard guard2;
    public static BlueGuard guard3;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand.setX(985.60016f);
        farrand.setY(785.5205f);
        farrand.setMovementType(MovementType.WALKING);
        Camera.setPos(Camera.translateToCameraCenter(farrand.getVector(), farrand.getHeight()));

        jeremiah.setX(1019.05994f);
        jeremiah.setY(760.96906f);
        jeremiah.setMovementType(MovementType.WALKING);

        guard1 = new BlueGuard();
        w.loadAndAdd(guard1);
        guard1.setX(924f);
        guard1.setY(725f);
        guard1.setMovementType(MovementType.WALKING);

        guard2 = new BlueGuard();
        w.loadAndAdd(guard2);
        guard2.setX(924f);
        guard2.setY(760f);
        guard2.setMovementType(MovementType.WALKING);

        guard3 = new BlueGuard();
        w.loadAndAdd(guard3);
        guard3.setX(924f);
        guard3.setY(795f);
        guard3.setMovementType(MovementType.WALKING);
    
        guard4 = new BlueGuard();
        w.loadAndAdd(guard4);
        guard4.setX(924f);
        guard4.setY(830f);
        guard4.setMovementType(MovementType.WALKING);
    
        guard5 = new BlueGuard();
        w.loadAndAdd(guard5);
        guard5.setX(924f);
        guard5.setY(865f);
        guard5.setMovementType(MovementType.WALKING);

        guard1.setHostile(false);
        guard2.setHostile(false);
        guard3.setHostile(false);
        guard4.setHostile(false);
        guard5.setHostile(false);

        w.setWorldBrightness(0.4f);
        w.createLight(0, 0, 0.1f, 0.1f);
    }

    @Override
    public void onDisplay(World world) {
        super.onDisplay(world);

        farrand.setX(985.60016f);
        farrand.setY(785.5205f);
        Camera.setPos(Camera.translateToCameraCenter(farrand.getVector(), farrand.getHeight()));

        jeremiah.setX(1019.05994f);
        jeremiah.setY(760.96906f);
        jeremiah.setVisible(true);
    }

    @Override
    public void onRespawn(World oldworld) {

    }
}
