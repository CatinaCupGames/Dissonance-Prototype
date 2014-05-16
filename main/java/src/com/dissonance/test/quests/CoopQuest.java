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
import com.dissonance.game.quests.PauseQuest;
import com.dissonance.game.sprites.Enemy;
import com.dissonance.game.w.CityEntrySquare;
import com.dissonance.test.w.AICity;

import java.awt.*;

public class CoopQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("AICity");
        setWorld(world);
        world.waitForWorldDisplayed();

        Player player1 = Players.createPlayer1();
        player1.joinAs(AICity.farrand);

        for (Player player : Players.getPlayersWithInput()) {
            if (!player.isPlaying())
                player.join();
        }

        Enemy enemy = new Enemy("aloysius", Enemy.StatType.NON_MAGIC, CombatSprite.CombatType.CREATURE);
        world.loadAndAdd(enemy);
        enemy.setX(100f);
        enemy.setY(300f);
        ParticleSprite.createParticlesAt(100f, 300f, 5000f, 10f, Color.GREEN, world);

        AICity.farrand.setCurrentWeapon(Weapon.getWeapon("test").createItem(CityEntrySquare.farrand));

    }

    @Override
    public String getName() {
        return "coop";
    }
}
