package com.dissonance.test.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.w.GameWorldLoader;

public class parallax_test extends GameWorldLoader {
    public static Jeremiah var1;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        var1 = new Jeremiah();
        w.loadAndAdd(var1);

        farrand.select();

        farrand.setY(300);
    }
}
