package com.dissonance.game.w;

import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.TestPlayer;

public class GameWorldLoader implements WorldLoader {
    @Override
    public void onLoad(World w) {
        //TODO Always load any hud's that will be displayed during the game
        HUD hud = new HUD("->hud");
        w.addDrawable(hud);


        //TODO Always load the player and the party here
        TestPlayer p = new TestPlayer();
        w.loadAnimatedTextureForSprite(p);
        w.addSprite(p);
        p.setWorld(w);
        p.setX(256);
        p.setY(256);
        p.select();
    }
}
