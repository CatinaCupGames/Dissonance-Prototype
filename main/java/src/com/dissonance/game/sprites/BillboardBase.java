package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

public class BillboardBase extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "billboardbase";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("test");
        playAnimation();
    }
}
