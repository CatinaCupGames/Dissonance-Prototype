package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.menu.catworld;

public class CatQuest extends AbstractQuest {
    public static CatQuest INSTANCE;
    @Override
    public void startQuest() throws Exception {
        INSTANCE = this;

        World world = WorldFactory.getWorld("menu.catworld", false);
        setWorld(world);
        world.waitForWorldDisplayed();

        Thread.sleep(800L);

        catworld.logo.start();
    }

    public void fadeOut() {
        RenderService.INSTANCE.fadeToBlack(1500f);
        try {
            RenderService.INSTANCE.waitForFade();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setNextQuest(new MenuQuest());
        try {
            endQuest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "cats";
    }
}
