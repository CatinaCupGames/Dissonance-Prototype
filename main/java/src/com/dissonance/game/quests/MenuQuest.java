package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.menus.GameMenu;

import java.util.Random;

public class MenuQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("main_menu_world");
        setWorld(w);
        w.waitForWorldLoaded();
        System.out.println("loaded!");
        Thread.sleep(5000);
        for (int i = 0; i < 3; i++) {
            RenderService.INSTANCE.fadeToAlpha(300, 0.7f);
            Thread.sleep(300 + 500);
            RenderService.INSTANCE.fadeFromBlack(300);
            Thread.sleep(300 + 500);
        }
    }

    @Override
    public String getName() {
        return "main_menu";
    }
}
