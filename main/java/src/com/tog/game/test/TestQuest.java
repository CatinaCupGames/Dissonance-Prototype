package com.tog.game.test;

import com.tog.framework.game.AbstractQuest;
import com.tog.framework.game.sprites.impl.TestPlayer;
import com.tog.framework.game.world.World;
import com.tog.framework.game.world.WorldFactory;
import com.tog.framework.system.exceptions.WorldLoadFailedException;

public class TestQuest extends AbstractQuest {
    @Override
    public void startQuest() {
        try {
            World w = WorldFactory.getWorld("test_world");
            setWorld(w);
            TestPlayer p = new TestPlayer();
            w.loadAnimatedTextureForSprite(p);
            w.addSprite(p);
            p.setX(0);
            p.setY(0);
            p.select();
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            World w = WorldFactory.getWorld("test_world");
            setWorld(w);
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            endQuest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        /**
         * This is a stress test for sprite sorting.
         * In this test, x amount of TestPlayer objects are created and drawn on the screen.
         * Every 20 seconds, the player you play as is changed.
         *
         * It seems the current sorting method is perfectly fine. Feel free to play
         * around with the STRESS_COUNT value.
         */
        /*final World w = getWorld();
        final int STRESS_COUNT = 10;
        final Random random = new Random();
        List<TestPlayer> testPlayers = new ArrayList<TestPlayer>();
        for (int i = 0; i < STRESS_COUNT; i++) {
            TestPlayer p = new TestPlayer();
            w.loadAnimatedTextureForSprite(p);
            w.addSprite(p);
            p.setX(random.nextInt(300));
            p.setY(random.nextInt(300));
            testPlayers.add(p);
        }

        w.invalidateDrawableList();
        for (TestPlayer testPlayer : testPlayers) {
            testPlayer.select();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}
