package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;

public class Billboard extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "billboard";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("billboard1");
        playAnimation();

        BillboardBase base = new BillboardBase();
        base.setX(getX());
        base.setY(getY() + getHeight() + 10f);
    }
}
