package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;

public class MenuQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("menu.MainMenu");
        setWorld(world);
        world.waitForWorldDisplayed();
        RenderService.INSTANCE.fadeToBlack(1f);
        RenderService.INSTANCE.fadeFromBlack(2500);
        //TODO Display menu and wait for option to be chosen

        //TODO Remove, temp code
        //setNextQuest(new DisclaimerQuest()); //Set the next quest
        //endQuest(); //End this quest
    }

    @Override
    public String getName() {
        return "main_menu";
    }
}
