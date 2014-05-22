package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

/**
 * Created by john on 5/22/14.
 */
public class ControlRoom extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "ControlRoom";
    }
    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("doors");
        pauseAnimation();
    }
}
