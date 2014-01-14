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

        TextureLoader.setFastRedraw(false);
        TestNPC var1 = new TestNPC();
        w.loadAndAdd(var1);
        var1.setX(400f);
        var1.setY(600f);
        TextureLoader.setFastRedraw(true);
    }
}