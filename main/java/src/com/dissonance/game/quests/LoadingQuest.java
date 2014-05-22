package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.GameCache;

public class LoadingQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World someWorld = WorldFactory.getWorld("menu.loadworld");
        setWorld(someWorld);
        someWorld.waitForWorldDisplayed();
        RenderService.INSTANCE.fadeFromBlack(500);
        RenderService.INSTANCE.waitForFade();


        GameCache.RoofTopBeginning = WorldFactory.getWorld("FactoryFloorCat", false);
        GameCache.OutsideFighting = WorldFactory.getWorld("OutsideFightingshit", false);
        //GameCache.OutsideFightingshit.useExtreamSpeed(true);
        Thread.sleep(2000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                GameCache.RoofTopBeginning.prepareTiles();
            }
        }, true);

        Thread.sleep(3000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                GameCache.OutsideFighting.prepareTiles();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            setNextQuest(new GameQuest());
                            endQuest();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }, true);
    }

    @Override
    public String getName() {
        return null;
    }
}
