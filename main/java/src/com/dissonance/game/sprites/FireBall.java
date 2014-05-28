package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

public class FireBall extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "fireball_spell";
    }

    @Override
    public void onLoad(){
        super.onLoad();
        setAnimation("up");
        playAnimation();
    }
}
