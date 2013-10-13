package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.scene.hud.HUD;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.game.sprites.Enemy;
import com.dissonance.game.sprites.TestPlayer;

import java.util.ArrayList;
import java.util.List;
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
    public void startQuest() {
        try {
            World w = WorldFactory.getWorld("test_world");
            setWorld(w);
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }
        /*final Random random = new Random();
        Camera.setX(random.nextInt(300));
        Camera.setY(random.nextInt(300));
        Dialog d = DialogFactory.getDialog("TEST");
        DialogUI dialogUI = new DialogUI("TESTINGDIALOG--", d);
        dialogUI.displayUI();
        ///Camera.setPos(Camera.translateToCameraCenter(new Vec2(dialogUI.getX(), dialogUI.getY()), dialogUI.getWidth(), dialogUI.getHeight()));

        /*TestDialog test = new TestDialog();
        test.displayUI();
        Camera.setPos(Camera.translateToCameraCenter(new Vec2(test.getX(), test.getY()), test.getWidth(), test.getHeight()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test.close();*/

        /**
         * This is a stress quests for sprite sorting.
         * In this quests, x amount of TestPlayer objects are created and drawn on the screen.
         * Every 20 seconds, the player you play as is changed.
         *
         * It seems the current sorting method is perfectly fine. Feel free to play
         * around with the STRESS_COUNT value.
         */
        final World w = getWorld();

        HUD hud = new HUD("->hud");
        w.addDrawable(hud);

        final Random random = new Random();
        final int STRESS_COUNT = 3;
        List<TestPlayer> testPlayers = new ArrayList<TestPlayer>();
        for (int i = 0; i < STRESS_COUNT; i++) {
            TestPlayer p = new TestPlayer();
            w.loadAnimatedTextureForSprite(p);
            w.addSprite(p);
            p.setX(random.nextInt(300));
            p.setY(random.nextInt(300));
            testPlayers.add(p);
            if (i > 0) {
                Position pos = new Position(testPlayers.get(0).getVector());
                p.setWaypoint(pos);
            }
        }

        w.invalidateDrawableList();
        for (TestPlayer testPlayer : testPlayers) {
            testPlayer.select();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        /*
        final World w = getWorld();

        TestPlayer p = new TestPlayer();
        w.loadAnimatedTextureForSprite(p);
        w.addSprite(p);
        p.setX(-100);
        p.setY(0);
        p.select();
        p.freeze();

        TestScene testScene = new TestScene();
        testScene.beginScene();
        p.unfreeze();
        TestPlayer pp = new TestPlayer();
        w.loadAnimatedTextureForSprite(pp);
        w.addSprite(pp);
        pp.setX(-100);
        pp.setY(200);

        TestNPC npc = new TestNPC("arrem");
        w.loadAnimatedTextureForSprite(npc);
        w.addSprite(npc);
        npc.setX(-200);
        npc.setY(200);

        Enemy enemy1 = new Enemy("enemy1", Enemy.StatType.NON_MAGIC, CombatSprite.CombatType.CREATURE, testEnemyInterface);
        w.loadAnimatedTextureForSprite(enemy1);
        w.addSprite(enemy1);
        enemy1.setX(-250);
        enemy1.setY(200);

        Enemy enemy2 = new Enemy("enemy2", Enemy.StatType.MAGIC, CombatSprite.CombatType.CREATURE, testEnemyInterface);
        w.loadAnimatedTextureForSprite(enemy2);
        w.addSprite(enemy2);
        enemy2.setX(-300);
        enemy2.setY(200);*/
    }
}
