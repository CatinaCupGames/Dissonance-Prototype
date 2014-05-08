package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.GateScene;
import com.dissonance.game.w.CityEntrySquare;

public class GateQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {

        CityEntrySquare.farrand.freeze();
        CityEntrySquare.farrand.select();
        playSceneAndWait(GateScene.class);
        CityEntrySquare.farrand.unfreeze();
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
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(CityEntrySquare.farrand.getX());
                    if(CityEntrySquare.farrand.getX() <= 256f){
                        //CityEntrySquare.farrand.freeze();
                        RenderService.INSTANCE.fadeToBlack(1500);
                        setNextQuest(new HallwayQuest());
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

}
