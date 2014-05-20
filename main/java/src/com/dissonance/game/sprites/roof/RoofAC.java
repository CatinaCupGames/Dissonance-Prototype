package com.dissonance.game.sprites.roof;

import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class RoofAC extends ImagePhysicsSprite{
    public RoofAC(){super("sprites/img/RoofAirConditioner.png");}

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
