package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building5 extends ImagePhysicsSprite {
    public Building5() {
        super("sprites/buildings/building5.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building5.txt";
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
