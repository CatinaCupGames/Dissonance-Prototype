package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.Input;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.game.player.input.joypad.JoypadService;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.game.sprites.Enemy;
import com.dissonance.game.w.CityEntrySquare;

import java.awt.*;

public class CoopQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("AICity");
        setWorld(world);
        world.waitForWorldDisplayed();

        Player player1 = Players.createPlayer1();
        player1.joinAs(CityEntrySquare.farrand);

        for (Player player : Players.getPlayersWithInput()) {
            player.join();
        }

        Enemy enemy = new Enemy("aloysius", Enemy.StatType.NON_MAGIC, CombatSprite.CombatType.CREATURE);
        world.loadAndAdd(enemy);
        enemy.setX(100f);
        enemy.setY(300f);
        ParticleSprite.createParticlesAt(100f, 300f, 5000f, 10f, Color.GREEN, world);

        CityEntrySquare.farrand.setCurrentWeapon(Weapon.getWeapon("test").createItem(CityEntrySquare.farrand));

    }

    @Override
    public String getName() {
        return "coop";
    }
}
