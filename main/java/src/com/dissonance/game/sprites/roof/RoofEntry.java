package com.dissonance.game.sprites.roof;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class RoofEntry extends ImagePhysicsSprite {
    public RoofEntry(){super("sprites/img/roofentry.png");}

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
