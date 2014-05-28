package com.dissonance.game.w;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.Admin;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.environment.BasicLight;
import com.dissonance.game.sprites.factory.Key;

import java.io.File;
import java.util.ArrayList;

public class FactoryFloorCat extends DemoLevelWorldLoader {
    /*
    LAYER 2 = PLAYER
    LAYER 6 = CATWALK LAYER
     */
    public static NodeMap groundNodeMap;
    public static NodeMap nongroundNodeMap;
    private static final float WALL_LIGHT_BRIGHTNESS = 1.4f;
    public static BlueGuard[] melees = new BlueGuard[5];
    public static Admin miniboss;

    @Override
    public void onLoad(World w) {
        ArrayList<Layer> ground = new ArrayList<>();
        ArrayList<Layer> nonground = new ArrayList<>();
        Layer[] layers = w.getLayers(LayerType.TILE_LAYER);
        for (Layer layer : layers) {
            if (layer.isGroundLayer())
                ground.add(layer);
            else
                nonground.add(layer);
        }

        groundNodeMap = new NodeMap(w, w.getWidth(), w.getHeight());
        groundNodeMap.setCachePath("cache" + File.separator + "factory_GROUND.nodes");
        groundNodeMap.create(ground.toArray(new Layer[ground.size()]));

        nongroundNodeMap = new NodeMap(w, w.getWidth(), w.getHeight());
        nongroundNodeMap.setCachePath("cache" + File.separator + "factory_NONGROUND.nodes");
        nongroundNodeMap.create(nonground.toArray(new Layer[nonground.size()]));

        /*miniboss = new Admin();
        miniboss.setLayer(6);
        miniboss.setX(33f * 16f);
        miniboss.setY(27f * 16f);
        w.loadAndAdd(miniboss);*/

        BlueGuard guard = new BlueGuard();
        guard.setX(28f * 16f);
        guard.setY(38f * 16f);
        guard.setLayer(6);
        w.loadAndAdd(guard);

        melees[0] = new BlueGuard();
        w.loadAndAdd(melees[0]);
        melees[0].setX(54*16);
        melees[0].setY(184*16);

        melees[1] = new BlueGuard();
        w.loadAndAdd(melees[1]);
        melees[1].setX(31*16);
        melees[1].setY(171*16);

        melees[2] = new BlueGuard();
        w.loadAndAdd(melees[2]);
        melees[2].setX(14*16);
        melees[2].setY(153*16);

        melees[3] = new BlueGuard();
        w.loadAndAdd(melees[3]);
        melees[3].setX(40*16);
        melees[3].setY(116*16);

        melees[4] = new BlueGuard();
        w.loadAndAdd(melees[4]);
        melees[4].setX(13*16);
        melees[4].setY(50*16);

        for (BlueGuard melee : melees) {
            melee.setLayer(2);
        }
    }

    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        w.setWorldBrightness(0.9f);

        Key key = new Key();
        key.setX(38f * 16f);
        key.setY(72f * 16f);
        key.setLayer(2);
        w.loadAndAdd(key);

        farrand.setX(5f * 16f);
        farrand.setY(208f * 16f);
        farrand.setLayer(2);
        farrand.setVisible(false);

        jeremiah.setX(7f * 16f);
        jeremiah.setY(208f * 16f);
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
