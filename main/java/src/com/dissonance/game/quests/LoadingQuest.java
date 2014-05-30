package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.GameCache;

public class LoadingQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World someWorld = WorldFactory.getWorld("menu.loadworld");
        setWorld(someWorld);
        someWorld.waitForWorldDisplayed();
        RenderService.INSTANCE.fadeFromBlack(500);
        RenderService.INSTANCE.waitForFade();


        GameCache.RoofTopBeginning = WorldFactory.getWorld("RoofTopBeginning", false);
        GameCache.OutsideFighting = WorldFactory.getWorld("OutsideFighting", false);
        GameCache.FactoryFloor = WorldFactory.getWorld("FactoryFloorCat", false);
        GameCache.RooftopMid = WorldFactory.getWorld("RooftopMid", false);
        GameCache.OfficeFloor1 = WorldFactory.getWorld("OfficeFloor1", false);
        GameCache.OfficeFloor2 = WorldFactory.getWorld("officefloor2", false);

        Thread.sleep(2000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {

                boolean value = GameSettings.Graphics.useFBO;
                GameSettings.Graphics.useFBO = false;

                GameCache.RoofTopBeginning.prepareTiles();
                GameCache.RooftopMid.prepareTiles();

                GameSettings.Graphics.useFBO = value;
            }
        }, true);

        Thread.sleep(3000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                GameCache.OutsideFighting.prepareTiles();
            }
        }, true);

        Thread.sleep(4000);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                GameCache.FactoryFloor.prepareTiles();
                GameCache.OfficeFloor1.prepareTiles();
                GameCache.OfficeFloor2.prepareTiles();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);


                            setNextQuest(new OfficeQuest());

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
        Sound.stopSound("introtheme");
    }

    @Override
    public String getName() {
        return null;
    }
}
