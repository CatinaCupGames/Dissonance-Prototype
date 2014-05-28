package com.dissonance.game.sprites.roof;

import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class RoofFan extends ImagePhysicsSprite {
    public RoofFan(){super("sprites/img/RoofFan.png");}

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
