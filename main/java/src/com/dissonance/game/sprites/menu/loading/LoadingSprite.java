package com.dissonance.game.sprites.menu.loading;

import com.dissonance.game.sprites.ImageSprite;

public class LoadingSprite extends ImageSprite {
    public LoadingSprite() {
        super("sprites/menu/Menus/Loading_Screen.png");
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
