package com.dissonance.game.sprites.roof;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class RoofPipe extends ImagePhysicsSprite {
    public RoofPipe(){super("sprites/img/themariotunnel.png");}

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
