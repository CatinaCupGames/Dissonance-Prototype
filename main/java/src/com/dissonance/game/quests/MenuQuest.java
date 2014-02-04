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
        //TODO Display menu and wait for option to be chosen

        //TODO Remove, temp code
        setNextQuest(new Demo_Level1_Quest()); //Set the next quest
        endQuest(); //End this quest
    }

    @Override
    public String getName() {
        return "main_menu";
    }
}
