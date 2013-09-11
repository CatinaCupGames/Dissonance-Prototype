package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.game.scene.TestScene;
import com.dissonance.game.sprites.TestNPC;
import com.dissonance.game.sprites.TestPlayer;

public class TestQuest extends AbstractQuest {
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
        /*final World w = getWorld();
        final int STRESS_COUNT = 2;
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
            }
        }*/

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
        pp.setX(-200);
        pp.setY(200);
    }
}
