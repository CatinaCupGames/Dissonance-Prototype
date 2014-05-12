package com.dissonance.game.w;

import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.hud.BaseHUD;

/**
 * This "World Loader" is an abstract World Loader that adds the Player and any HUD to the world.
 * Other World Loaders can extend this "World Loader" and execute super.onLoad(world) to use this
 * World Loader.
 */
public abstract class GameWorldLoader implements WorldLoader {
    public static BaseHUD hud;
    public static Player player1;
    //public static HUD hud;
    //public static Wyatt wyatt;
    public static Farrand farrand;
    @Override
    public void onLoad(World w) {
        if (hud == null) {
            hud = new BaseHUD();
            hud.display(w);
        }

        if (player1 == null) {
            player1 = Players.createPlayer1();
        }

        if (farrand == null) {
            farrand = new Farrand();
            w.loadAndAdd(farrand);
        } else {
            w.loadAndAdd(farrand);
            Camera.setPos(Camera.translateToCameraCenter(farrand.getVector(), farrand.getHeight()));
        }
    }

    @Override
    public void onDisplay(World w) { }
}
