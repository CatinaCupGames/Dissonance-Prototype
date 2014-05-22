package com.dissonance.game.sprites.factory;

import com.dissonance.game.sprites.ImagePhysicsSprite;

/**
 * Created by john on 5/22/14.
 */
public class Crate extends ImagePhysicsSprite {
    public Crate() {
        super("sprites/img/Crate.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
