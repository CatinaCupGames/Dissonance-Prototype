package com.dissonance.game.w;

import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.hud.BaseHUD;

/**
 * This "World Loader" is an abstract World Loader that adds the Player and any HUD to the world.
 * Other World Loaders can extend this "World Loader" and execute super.onLoad(world) to use this
 * World Loader.
 */
public abstract class GameWorldLoader implements WorldLoader {
    public static BaseHUD hud;
    public static Farrand farrand;
    public static Jeremiah jeremiah;


    @Override
    public void onLoad(World w) {
        if (hud == null) {
            hud = new BaseHUD(Players.createPlayer1());
            hud.display(w);
        }
        if (farrand == null) {
            farrand = new Farrand();
        }
        if (jeremiah == null) {
            jeremiah = new Jeremiah();
        }

        if (!jeremiah.isAlly(farrand))
            jeremiah.joinParty(farrand);
    }

    @Override
    public void onDisplay(World w) {
        w.loadAndAdd(farrand);
        w.loadAndAdd(jeremiah);
    }
}
