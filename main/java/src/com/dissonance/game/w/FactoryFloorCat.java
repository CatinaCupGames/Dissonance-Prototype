package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.environment.BasicLight;

public class FactoryFloorCat extends DemoLevelWorldLoader {
    /*
    LAYER 2 = PLAYER
    LAYER 6 = CATWALK LAYER
     */

    private static final float WALL_LIGHT_BRIGHTNESS = 1.4f;

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        w.setWorldBrightness(0.9f);

        farrand.setX(52f * 16f);
        farrand.setY(175f * 16f);
        farrand.setLayer(2);

        jeremiah.setX(53f * 16f);
        jeremiah.setY(176f * 16f);
        jeremiah.setLayer(2);
        jeremiah.setVisible(false);

        if (GameSettings.Graphics.qualityLights) {
            w.createLight(25*16, 199*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(40*16, 161*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(20*16, 161*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(24*16, 138*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(40*16, 138*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(24*16, 122*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(39*16, 112*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(24*16, 112*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(12*16, 112*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(34*16, 81*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(11*16, 81*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(8*16, 57*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(16*16, 32*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(30*16, 32*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(43*16, 32*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
            w.createLight(54*16, 24*16, WALL_LIGHT_BRIGHTNESS, 0.5f);
        } else {
            w.setWorldBrightness(0.4f);
            w.createLight(-200, -200, 0.4f, 0.1f); //Lights for brightness

            createLight(25*16, 199*16, w);
            createLight(40*16, 161*16, w);
            createLight(20*16, 161*16, w);
            createLight(24*16, 138*16, w);
            createLight(40*16, 138*16, w);
            createLight(24*16, 122*16, w);
            createLight(39*16, 112*16, w);
            createLight(24*16, 112*16, w);
            createLight(12*16, 112*16, w);
            createLight(34*16, 81*16,  w);
            createLight(11*16, 81*16,  w);
            createLight(8*16, 57*16,   w);
            createLight(16*16, 32*16,  w);
            createLight(30*16, 32*16,  w);
            createLight(43*16, 32*16,  w);
            createLight(54*16, 24*16,  w);
        }
    }

    private void createLight(float x, float y, World w) {
        BasicLight light = new BasicLight();
        light.setX(x);
        light.setY(y);
        light.setStartHeight(256f);
        light.setStartWidth(256f);
        light.setAlpha(0.4f);
        w.loadAndAdd(light);
    }
}
