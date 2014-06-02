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
        int property = getPropertyAsInt("type");
        if (property == 0)
            property = 1;

        setAnimation("billboard" + property);
        playAnimation();

        setLayer(2);

        BillboardBase base = new BillboardBase();
        base.setX(getX());
        base.setY(getY() + getHeight() - 20f);
        getWorld().loadAndAdd(base);
    }
}
