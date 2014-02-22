package com.dissonance.game.w;

import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.Farrand;

/**
 * This "World Loader" is an abstract World Loader that adds the Player and any HUD to the world.
 * Other World Loaders can extend this "World Loader" and execute super.onLoad(world) to use this
 * World Loader.
 */
public abstract class GameWorldLoader implements WorldLoader {
    public static HUD hud;
    //public static Wyatt wyatt;
    public static Farrand farrand;
    @Override
    public void onLoad(World w) {
        if (farrand == null) {
            farrand = new Farrand();
            w.loadAndAdd(farrand);
            farrand.select();
        } else {
            w.loadAndAdd(farrand);
            Camera.setPos(Camera.translateToCameraCenter(farrand.getVector(), farrand.getHeight()));
            if (!farrand.isSelected()) //Maybe remove
                farrand.select();
        }
    }

    @Override
    public void onDisplay(World w) { }
}
