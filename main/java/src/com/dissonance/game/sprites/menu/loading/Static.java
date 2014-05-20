package com.dissonance.game.sprites.menu.loading;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

public class Static extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "static";
    }

    @Override
    public boolean neverClip() {
        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("static");
        playAnimation();
        setLayer(2);
    }
}
