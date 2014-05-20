package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building1 extends ImagePhysicsSprite {
    public Building1() {
        super("sprites/buildings/building.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building.txt";
    }
}
