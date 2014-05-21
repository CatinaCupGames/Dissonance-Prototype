package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.menu.loading.LoadingSprite;
import com.dissonance.game.sprites.menu.loading.Spinner;
import com.dissonance.game.sprites.menu.loading.Static;

public class loadworld implements WorldLoader {
    @Override
    public void onLoad(World world) {
        LoadingSprite sprite = new LoadingSprite();
        sprite.setX(1024f / 2f);
        sprite.setY(512f / 2f);
        world.loadAndAdd(sprite);

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        world.loadAndAdd(s);

        Spinner spinner = new Spinner();
        spinner.setX(GameSettings.Display.game_width / 4f);
        spinner.setY(210);
        world.loadAndAdd(spinner);

    }

    @Override
    public void onDisplay(World world) {

    }
}
