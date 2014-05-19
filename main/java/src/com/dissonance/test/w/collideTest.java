package com.dissonance.test.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.w.DemoLevelWorldLoader;

public class collideTest extends DemoLevelWorldLoader {
    public static Jeremiah var1;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        var1 = new Jeremiah();
        w.loadAndAdd(var1);
        var1.setX(314.59985f);
        var1.setY(196.89998f);
    }
}