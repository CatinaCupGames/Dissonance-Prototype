package com.dissonance.game.test;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.dialog.TestDialog;
import com.dissonance.framework.game.sprites.impl.TestPlayer;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;

import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestQuest extends AbstractQuest {
    @Override
    public void startQuest() {
        try {
            World w = WorldFactory.getWorld("test_world");
            setWorld(w);
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }

        TestDialog test = new TestDialog();
        test.displayUI();
        Camera.setPos(Camera.translateToCameraCenter(new Vec2(test.getX(), test.getY()), test.getWidth(), test.getHeight()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test.close();

        /**
         * This is a stress test for sprite sorting.
         * In this test, x amount of TestPlayer objects are created and drawn on the screen.
         * Every 20 seconds, the player you play as is changed.
         *
         * It seems the current sorting method is perfectly fine. Feel free to play
         * around with the STRESS_COUNT value.
         */
        final World w = getWorld();
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
        }
    }
}
