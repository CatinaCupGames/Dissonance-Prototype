package com.dissonance.game.sprites.office;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

public class DeskRadio extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "deskradio";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("deskradio");
        playAnimation();
    }
}
