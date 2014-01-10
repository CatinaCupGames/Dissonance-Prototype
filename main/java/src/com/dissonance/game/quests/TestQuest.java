package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.scene.TestScene;
import com.dissonance.game.sprites.Enemy;

import java.util.Random;

public class TestQuest extends AbstractQuest {

    public static Enemy.AIInterface testEnemyInterface = new Enemy.AIInterface() {
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
        Sound.playSound("creepy").setOnSoundFinishedListener(new Sound.OnSoundFinishedListener() {
            @Override
            public void onFinished(Sound.SoundFinishedType type) {
                System.out.println("Creepy finished " + type);
            }
        });

        Sound.getSound("dialogadvance").setOnSoundFinishedListener(new Sound.OnSoundFinishedListener() {
            @Override
            public void onFinished(Sound.SoundFinishedType type) {
                System.out.println("Dialog advance finished " + type);
            }
        });

        World w = WorldFactory.getWorld("test_tileset");
        setWorld(w);
        w.waitForWorldLoaded();
        //Thread.sleep(5000);
        //TestScene scene = new TestScene();
        //scene.beginScene();
        //RenderService.INSTANCE.provideData(true, RenderService.ENABLE_CROSS_FADE);
        //RenderService.INSTANCE.provideData(3000f, RenderService.CROSS_FADE_DURATION);
        //World world = WorldFactory.getWorld("test_tileset2");
        //setWorld(world, RenderService.TransitionType.CROSSFADE);
    }

    @Override
    public String getName() {
        return "Test Quest";
    }
}
