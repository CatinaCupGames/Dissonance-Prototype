package com.dissonance.game.sprites.menu;

import com.dissonance.game.sprites.ImageSprite;

public class Banner extends ImageSprite {
    public Banner() {
        super("sprites/menu/Menus/banner.png");
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
