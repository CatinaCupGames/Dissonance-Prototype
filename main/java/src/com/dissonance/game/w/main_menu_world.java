package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.menus.GameMenu;

public class main_menu_world implements WorldLoader {
    @Override
    public void onLoad(World world) {
        GameMenu gameMenu = new GameMenu("->menu<-");
        gameMenu.displayUI(world);
    }
}
