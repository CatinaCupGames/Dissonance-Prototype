package com.dissonance.game.sprites.menu;

import com.dissonance.game.sprites.ImageSprite;

public class Background extends ImageSprite {
    public Background() {
        super("sprites/menu/Menus/Generic_Screen.png");
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}