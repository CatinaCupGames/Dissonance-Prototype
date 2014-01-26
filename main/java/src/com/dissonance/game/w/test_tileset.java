/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/01/02 20:13:01
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.game.sprites.TestNPC;


public class test_tileset extends GameWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        TestNPC npc = new TestNPC();
        w.loadAndAdd(npc);
        npc.setX(200);
        npc.setY(300);

        TestNPC npc2 = new TestNPC();
        w.loadAndAdd(npc2);
        npc2.setX(240);
        npc2.setY(300);

        TestNPC npc3 = new TestNPC();
        w.loadAndAdd(npc3);
        npc3.setX(280);
        npc3.setY(300);
    }
}