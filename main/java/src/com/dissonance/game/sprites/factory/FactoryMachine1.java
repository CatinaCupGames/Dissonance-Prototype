package com.dissonance.game.sprites.factory;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class FactoryMachine1 extends ImagePhysicsSprite {
    public FactoryMachine1() {
        super("sprites/img/FactoryMachine1.png");
    }
    public FactoryMachine1(String img) { super(img); }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/FactoryMachine1.txt";
    }
}
