package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building4 extends ImagePhysicsSprite {
    public Building4() {
        super("sprites/buildings/building4.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
