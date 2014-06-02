package com.dissonance.game.w;

import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.environment.BasicLight;
import com.dissonance.game.sprites.hud.BaseHUD;

import java.awt.*;

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
        if (farrand == null) {
            farrand = new Farrand();
        }
        if (jeremiah == null) {
            jeremiah = new Jeremiah();
        }

        if (!jeremiah.isAlly(farrand))
            jeremiah.joinParty(farrand);

        farrand.setMovementType(MovementType.RUNNING);
        jeremiah.setMovementType(MovementType.RUNNING);
    }

    @Override
    public void onDisplay(World w) {
        if (!w.getAllCombatSprites().contains(farrand))
            w.loadAndAdd(farrand);
        else
            farrand.setWorld(w);
        if (!w.getAllCombatSprites().contains(jeremiah))
            w.loadAndAdd(jeremiah);
        else
            jeremiah.setWorld(w);

        huds[0] = new BaseHUD(Players.createPlayer1()); //Because fuck memory leaks
        huds[0].display(w);

        for (int i = 2; i <= 4; i++) {
            Player player = Players.getPlayer(i);
            if (player == null)
                break;
            huds[i - 1] = new BaseHUD(player);
            huds[i - 1].display(w);
        }
    }

    protected void createLight(World w, float x, float y, float brightness, float radius, float basicLightRadius, float basicLightAlpha, Color color) {
        if (!GameSettings.Graphics.qualityLights) {
            BasicLight light = new BasicLight();
            light.setX(x);
            light.setY(y);
            light.setStartHeight(basicLightRadius);
            light.setStartWidth(basicLightRadius);
            light.setAlpha(basicLightAlpha);
            light.setTint(color);
            w.loadAndAdd(light);
        } else {
            w.createLight(x, y, brightness, radius, color);
        }
    }

    protected void createLight(World w, float x, float y, float brightness, float radius, float basicLightRadius, float basicLightAlpha) {
        if (!GameSettings.Graphics.qualityLights) {
            BasicLight light = new BasicLight();
            light.setX(x);
            light.setY(y);
            light.setStartHeight(basicLightRadius);
            light.setStartWidth(basicLightRadius);
            light.setAlpha(basicLightAlpha);
            w.loadAndAdd(light);
        } else {
            w.createLight(x, y, brightness, radius);
        }
    }

    public void onRespawn(World world) {
        CombatSprite[] sprites = world.getAllCombatSprites().toArray(new CombatSprite[world.getAllCombatSprites().size()]);

        for (CombatSprite sprite : sprites) {
            if (sprite instanceof Jeremiah || sprite instanceof Farrand)
                continue;
            world.removeSprite(sprite);
        }

        farrand.refillHP();
        farrand.refillMP();

        jeremiah.refillHP();
        jeremiah.refillMP();
    }
}
