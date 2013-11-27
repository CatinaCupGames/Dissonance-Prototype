package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.TestPlayer;

public class arrem_world extends GameWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        TestPlayer pl;
        pl = new TestPlayer();
        w.loadAnimatedTextureForSprite(pl);
        w.addSprite(pl);
        pl.setWorld(w);
        pl.setX(576);
        pl.setY(256);
        pl.setWidth(pl.getWidth() * 2);
        pl.setHeight(pl.getHeight() * 2);
    }
}
