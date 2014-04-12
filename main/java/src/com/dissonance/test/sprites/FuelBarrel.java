package com.dissonance.test.sprites;

import com.dissonance.framework.game.sprites.impl.game.ExplodableSprite;

public final class FuelBarrel extends ExplodableSprite {

    @Override
    public String getSpriteName() {
        return "FuelBarrel";
    }

    @Override
    public String getExplosionName() {
        return "BarrelExplosion";
    }

    @Override
    public float getExplosionRadius() {
        return 320;
    }

    @Override
    public float getBaseDamage() {
        return 10;
    }
}
