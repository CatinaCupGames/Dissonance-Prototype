package com.dissonance.test.w;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.w.GameWorldLoader;

public class AICity extends GameWorldLoader {
    public static Farrand farrand;

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        farrand = new Farrand();
        w.loadAndAdd(farrand);
    }
}
