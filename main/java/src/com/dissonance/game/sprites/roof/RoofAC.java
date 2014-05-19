package com.dissonance.game.sprites.roof;

import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.game.sprites.ImageSprite;

public class RoofAC extends ImageSprite implements Collidable{
    public RoofAC(){super("sprites/img/RoofAirConditioner.png");}

    @Override
    public HitBox getHitBox() {
        return null;
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return false;
    }
}
