package com.dissonance.game.w;

import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.TestPlayer;
import com.dissonance.game.sprites.Wyatt;

/**
 * This "World Loader" is an abstract World Loader that adds the Player and any HUD to the world.
 * Other World Loaders can extend this "World Loader" and execute super.onLoad(world) to use this
 * World Loader.
 */
public abstract class GameWorldLoader implements WorldLoader {
    public static HUD hud;
    public static Wyatt wyatt;
    @Override
    public void onLoad(World w) {
        //TODO Always load any hud's that will be displayed during the game
        //if (hud == null)
        //    hud = new HUD("->hud");
        //hud.displayUI(false, w);

        if (wyatt == null) {
            wyatt = new Wyatt();
            w.loadAndAdd(wyatt);
            wyatt.select();
        } else {
            w.loadAndAdd(wyatt);
            Camera.setPos(Camera.translateToCameraCenter(wyatt.getVector(), 32));
        }
    }
}
