package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.menu.CatCutLogo;
import com.dissonance.game.sprites.menu.WhiteCover;

public class catworld implements WorldLoader {
    public static WhiteCover cover;
    public static CatCutLogo logo;
    @Override
    public void onLoad(World world) {
        cover = new WhiteCover();
        cover.setX(1024f / 2f);
        cover.setY(512f / 2f);
        cover.setAlpha(0f);
        cover.setLayer(0);
        world.loadAndAdd(cover);

        logo = new CatCutLogo();
        logo.setX(1024f / 2f);
        logo.setY(512f / 2f);
        logo.setAlpha(0f);
        logo.setLayer(1);
        world.loadAndAdd(logo);
    }

    @Override
    public void onDisplay(World world) {

    }
}
