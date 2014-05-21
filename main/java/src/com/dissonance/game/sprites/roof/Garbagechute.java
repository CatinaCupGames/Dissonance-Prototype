package com.dissonance.game.sprites.roof;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Garbagechute extends ImagePhysicsSprite {
    public Garbagechute() {
        super("sprites/img/garbagechute.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
