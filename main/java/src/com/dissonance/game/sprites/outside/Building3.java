package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building3 extends ImagePhysicsSprite {
    public Building3() {
        super("sprites/buildings/building3.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
