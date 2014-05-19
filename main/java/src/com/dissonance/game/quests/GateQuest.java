package com.dissonance.game.quests;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.LeaderFollow;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.GateScene;
import com.dissonance.game.w.CityEntrySquare;

public class GateQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        World world3 = WorldFactory.getWorld("CityEntrySquare");
        setWorld(world3);
        world3.waitForWorldDisplayed();
        world3.invalidateDrawableList(); //Because fuck you

        CityEntrySquare.farrand.freeze();
        CityEntrySquare.jeremiah.freeze();

        Player player1 = Players.createPlayer1();

        player1.joinAs(CityEntrySquare.farrand);

        Player player2;
        if ((player2 = Players.getPlayer(2)) != null) {
            player2.joinAs(CityEntrySquare.jeremiah);
        }

        playSceneAndWait(GateScene.class);
        CityEntrySquare.farrand.unfreeze();
        CityEntrySquare.jeremiah.unfreeze();
        WorldFactory.clearCache();
        final boolean[] moved = {false};
        CityEntrySquare.farrand.setSpriteMovedListener(new Sprite.SpriteEvent.SpriteMovedEvent() {
            @Override
            public void onSpriteMoved(Sprite sprite, float oldx, float oldy) {
                moved[0] = true;
                CityEntrySquare.farrand.setSpriteMovedListener(null);
                Camera.followSprite(CityEntrySquare.farrand);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(CityEntrySquare.farrand.getX() <= 256f){
                        RenderService.INSTANCE.fadeToBlack(1500);
                        try {
                            RenderService.INSTANCE.waitForFade();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setNextQuest(new Gate_2Quest());
                        try {
                            endQuest();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                }
            }
        }).start();

        followFarrand();

        do {
            Thread.sleep(10000);
            if (moved[0]) break;

            Dialog.displayDialog("movement1");

            Thread.sleep(30000);
            if (moved[0]) break;

            Dialog.displayDialog("movement2");

            Thread.sleep(2 * 60000);
            if (moved[0]) break;

            Dialog.displayDialog("movement3");

            Thread.sleep(60000);
        } while (true);

    }

    @Override
    public String getName() {
        return "player_movement_tutorial";

    }

    private void followFarrand() {
        CityEntrySquare.guard1.setBehavior(new LeaderFollow(CityEntrySquare.guard1, CityEntrySquare.farrand, new Vector(0f, 32f)));
        CityEntrySquare.guard2.setBehavior(new LeaderFollow(CityEntrySquare.guard2, CityEntrySquare.farrand, new Vector(32f, 32f)));
        CityEntrySquare.guard3.setBehavior(new LeaderFollow(CityEntrySquare.guard3, CityEntrySquare.farrand, new Vector(32f, 0f)));
        CityEntrySquare.guard4.setBehavior(new LeaderFollow(CityEntrySquare.guard4, CityEntrySquare.farrand, new Vector(32f, -32f)));
        CityEntrySquare.guard5.setBehavior(new LeaderFollow(CityEntrySquare.guard5, CityEntrySquare.farrand, new Vector(0f, -32f)));
        CityEntrySquare.jeremiah.setBehavior(new LeaderFollow(CityEntrySquare.jeremiah, CityEntrySquare.farrand, new Vector(0f, 16f)));

        CityEntrySquare.guard1.ignoreCollisionWith(CityEntrySquare.jeremiah, CityEntrySquare.guard2, CityEntrySquare.guard3, CityEntrySquare.guard4, CityEntrySquare.guard5, CityEntrySquare.farrand);
        CityEntrySquare.guard2.ignoreCollisionWith(CityEntrySquare.jeremiah, CityEntrySquare.guard1, CityEntrySquare.guard3, CityEntrySquare.guard4, CityEntrySquare.guard5, CityEntrySquare.farrand);
        CityEntrySquare.guard3.ignoreCollisionWith(CityEntrySquare.jeremiah, CityEntrySquare.guard2, CityEntrySquare.guard1, CityEntrySquare.guard4, CityEntrySquare.guard5, CityEntrySquare.farrand);
        CityEntrySquare.guard4.ignoreCollisionWith(CityEntrySquare.jeremiah, CityEntrySquare.guard2, CityEntrySquare.guard3, CityEntrySquare.guard1, CityEntrySquare.guard5, CityEntrySquare.farrand);
        CityEntrySquare.guard5.ignoreCollisionWith(CityEntrySquare.guard2, CityEntrySquare.guard3, CityEntrySquare.guard4, CityEntrySquare.guard1, CityEntrySquare.farrand, CityEntrySquare.jeremiah);
        CityEntrySquare.farrand.ignoreCollisionWith(CityEntrySquare.guard1, CityEntrySquare.guard2, CityEntrySquare.guard3, CityEntrySquare.guard4, CityEntrySquare.guard5, CityEntrySquare.jeremiah);
        CityEntrySquare.jeremiah.ignoreCollisionWith(CityEntrySquare.guard1, CityEntrySquare.guard2, CityEntrySquare.guard3, CityEntrySquare.guard4, CityEntrySquare.guard5, CityEntrySquare.farrand);

        CityEntrySquare.guard1.setMovementSpeed(20f);
        CityEntrySquare.guard2.setMovementSpeed(20f);
        CityEntrySquare.guard3.setMovementSpeed(20f);
        CityEntrySquare.guard4.setMovementSpeed(20f);
        CityEntrySquare.guard5.setMovementSpeed(20f);


    }

}
