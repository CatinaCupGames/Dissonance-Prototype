package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.menu.Background;
import com.dissonance.game.sprites.menu.Banner;
import com.dissonance.game.sprites.menu.CoopMenu;
import com.dissonance.game.sprites.menu.Static;
import com.dissonance.game.sprites.menu.buttons.BackButton;
import com.dissonance.game.sprites.menu.buttons.CoopPlayButton;

public class CoopMenuWorld implements WorldLoader{
    public static CoopMenu menu;
    @Override
    public void onLoad(World world) {

        Background b = new Background();
        b.setX(1024f / 2f);
        b.setY(512f / 2f);
        b.setLayer(0);
        world.loadAndAdd(b);

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(2);
        world.loadAndAdd(s);

        menu = new CoopMenu();
        menu.display(world);

        BackButton back = new BackButton();
        back.display(world);

        CoopPlayButton playButton = new CoopPlayButton();
        playButton.display(world);
    }

    @Override
    public void onDisplay(World world) {

    }
}
