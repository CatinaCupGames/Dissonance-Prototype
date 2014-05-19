package com.dissonance.game.w;

import com.dissonance.framework.game.player.Player;
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
public abstract class DemoLevelWorldLoader implements WorldLoader {
    public static BaseHUD[] huds = new BaseHUD[4];
    public static Farrand farrand;
    public static Jeremiah jeremiah;


    @Override
    public void onLoad(World w) {
        if (huds[0] == null) {
            huds[0] = new BaseHUD(Players.createPlayer1());
            huds[0].display(w);
        }
        for (int i = 2; i <= 4; i++) {
            Player player = Players.getPlayer(i);
            if (player == null)
                break;
            huds[i - 1] = new BaseHUD(player);
            huds[i - 1].display(w);
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
