package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;

public class BillboardBase extends PhysicsSprite {
    @Override
    public String getSpriteName() {
        return "billboardbase";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("test");
        playAnimation();

        setCutOffMargin(80);
    }
}
