package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.menus.GameMenu;

public class MenuQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        setWorld(WorldFactory.getWorld("main_menu_world"));
    }

    @Override
    public String getName() {
        return "main_menu";
    }
}
