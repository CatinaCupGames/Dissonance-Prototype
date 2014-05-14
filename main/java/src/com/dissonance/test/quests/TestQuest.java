package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.sprites.Enemy;

public class TestQuest extends AbstractQuest {

    @Override
    public void startQuest() throws Exception {
       Sound sound = Sound.fadeInSound("creepy", 4000).setOnSoundFinishedListener(new Sound.OnSoundFinishedListener() {
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
        w.waitForWorldDisplayed();
        //playSceneAndWait(SimpleSceneTest.class);
        final PlayableSprite player = PlayableSprite.getCurrentlyPlayingSprite();
        player.setMarksmanship(2);
        player.setMovementType(MovementType.RUNNING);
        player.glow();
        Camera.removeBounds();

        System.out.println(player.getMarksmanship());
        player.setX(100);
        player.setY(100);
        WeaponItem item = new WeaponItem(player, Weapon.getWeapon("test"));
        player.setCurrentWeapon(item);
        Enemy enemy = new Enemy("aloysius", Enemy.StatType.NON_MAGIC, CombatSprite.CombatType.CREATURE);
        w.loadAndAdd(enemy);
        enemy.setX(100f);
        enemy.setY(200f);

        Thread.sleep(3000);
        sound.fadeOut(5000);
        //enemy.setBehavior(new BehaviorOffsetFollow(enemy, player, new Position(10, 10)));
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
