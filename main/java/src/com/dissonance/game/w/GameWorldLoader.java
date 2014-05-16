package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.hud.BaseHUD;

/**
 * This "World Loader" is an abstract World Loader that adds the Player and any HUD to the world.
 * Other World Loaders can extend this "World Loader" and execute super.onLoad(world) to use this
 * World Loader.
 */
public abstract class GameWorldLoader implements WorldLoader {
    public static BaseHUD hud;
    //public static HUD hud;
    //public static Wyatt wyatt;
    @Override
    public void onLoad(World w) {
        if (hud == null) {
            hud = new BaseHUD();
            hud.display(w);
        }
    }

    @Override
    public void onDisplay(World w) { }
}
