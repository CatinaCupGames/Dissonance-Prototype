package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.game.GameCache;

public class LoadingQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World someWorld = WorldFactory.getWorld("loadworld");
        setWorld(someWorld);
        someWorld.waitForWorldDisplayed();

        Thread.sleep(4000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                try {
                    GameCache.RoofTopBeginning = WorldFactory.getWorld("RoofTopBeginning", false);
                    GameCache.RoofTopBeginning.prepareTiles();
                } catch (WorldLoadFailedException e) {
                    e.printStackTrace();
                }
            }
        }, true);

        Thread.sleep(2000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                try {
                    GameCache.OutsideFighting = WorldFactory.getWorld("OutsideFighting", false);
                    GameCache.OutsideFighting.prepareTiles();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                setNextQuest(new DisclaimerQuest());
                                endQuest();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (WorldLoadFailedException e) {
                    e.printStackTrace();
                }
            }
        }, true);
    }

    @Override
    public String getName() {
        return null;
    }
}
