package com.dissonance.test.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.menus.GameMenu;

/**
 * This World Loader adds a GameMenu UIElement to the World after it's been loaded.
 * Usually, main_menu_world is just an empty World with no Tiled map data linked to it, so only this code is
 * executed.
 */
public class main_menu_world implements WorldLoader {
    @Override
    public void onLoad(World world) {
        GameMenu gameMenu = new GameMenu();
        gameMenu.displayUI(world);
    }

    @Override
    public void onDisplay(World world) { }
}
