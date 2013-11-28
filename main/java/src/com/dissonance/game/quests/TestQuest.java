package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.game.scene.TestScene;
import com.dissonance.game.sprites.Enemy;
import com.dissonance.game.sprites.TestPlayer;

import java.util.Random;

public class TestQuest extends AbstractQuest {

    private Enemy.AIInterface testEnemyInterface = new Enemy.AIInterface() {
        private Random random = new Random();

        @Override
        public void onUpdate(Enemy enemy) {
            if (random.nextInt(100) == 3) {
                enemy.setX(enemy.getX() + (random.nextBoolean() ? random.nextInt(4) : -random.nextInt(4)));
            }
            if (random.nextInt(100) == 3)
                enemy.setY(enemy.getY() + (random.nextBoolean() ? random.nextInt(4) : -random.nextInt(4)));
        }
    };

    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("arrem_world");
        setWorld(w);
        Thread.sleep(3000);
        w = WorldFactory.getWorld("test_world");
        setWorld(w);
    }

    @Override
    public String getName() {
        return "Test Quest";
    }
}
