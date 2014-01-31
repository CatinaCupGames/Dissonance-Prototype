/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/01/02 20:13:01
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.shader.impl.Light;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.game.sprites.TestNPC;

import java.awt.*;


public class test_tileset extends GameWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        TestNPC npc3 = new TestNPC();
        w.loadAndAdd(npc3);
        npc3.setX(100);
        npc3.setY(100);
        final long start = System.currentTimeMillis();

        w.createLight(500f, 500f, 1.6f, 0.3f);

        final Light light = w.createLight(300, 300, 1.6f, 0.4f, Color.GREEN);
        w.getRenderService().runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                long sec = System.currentTimeMillis() - start;
                sec /= 500;
                float r = Math.min(0.3f + 0.5f * (float) Math.cos(sec), 1f);
                r = Math.max(r, 0f);
                float g = Math.min(0.3f + 0.5f * (float) Math.sin(sec), 1f);
                g = Math.max(g, 0f);
                //float b = Math.min(0.3f + 0.5f*(float)Math.tan(sec), 1f);
                //b = Math.max(b, 0f);
                light.setColor(new Color(r, g, 1f));
            }
        }, true, true);

        //w.createLight(1f, 1f, 1.3f, 3f);
        w.setWorldBrightness(0.7f);
    }
}