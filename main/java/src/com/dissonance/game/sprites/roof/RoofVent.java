package com.dissonance.game.sprites.roof;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class RoofVent extends ImagePhysicsSprite {
    public RoofVent(){super("sprites/img/roofvent.png");}

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
