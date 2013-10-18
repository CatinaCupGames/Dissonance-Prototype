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
    public static float xx = 0, yy = 0;
    public static TestPlayer pl;

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
            World w = WorldFactory.getWorld("arrem_world");
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

        pl = new TestPlayer();
        w.loadAnimatedTextureForSprite(pl);
        w.addSprite(pl);
        pl.setWorld(w);
        pl.setX(576);
        pl.setY(256);
        pl.setWidth(pl.getWidth() * 2);
        pl.setHeight(pl.getHeight() * 2);


        TestPlayer p = new TestPlayer();
        w.loadAnimatedTextureForSprite(p);
        w.addSprite(p);
        p.setWorld(w);
        p.setX(256);
        p.setY(256);
        p.select();

        w.invalidateDrawableList();
    }
}
