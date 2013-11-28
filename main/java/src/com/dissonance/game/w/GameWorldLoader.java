package com.dissonance.game.w;

import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.TestPlayer;
import com.dissonance.game.sprites.Wyatt;

public class GameWorldLoader implements WorldLoader {
    public static HUD hud;
    public static Wyatt wyatt;
    @Override
    public void onLoad(World w) {
        //TODO Always load any hud's that will be displayed during the game
        if (hud == null)
            hud = new HUD("->hud");
        hud.displayUI(false, w);


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
