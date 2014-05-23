package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.BlueGuard;

public class OutsideFighting extends DemoLevelWorldLoader {
    public static BlueGuard var4;
    public static BlueGuard var3;
    public static BlueGuard var2;
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        var4.setX(201f*16f);
        var4.setY(201f*16f);

        farrand.setX(200f*16f);
        farrand.setY(200f*16f);
    }
}
