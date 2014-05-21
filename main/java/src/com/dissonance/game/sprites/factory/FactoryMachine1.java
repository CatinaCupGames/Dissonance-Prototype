package com.dissonance.game.sprites.factory;

import com.dissonance.game.sprites.ImagePhysicsSprite;

/**
 * Created by Jmerrill on 5/20/2014.
 */
public class FactoryMachine1 extends ImagePhysicsSprite {
    public FactoryMachine1() {
        super("sprites/img/FactoryMachine1.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/FactoryMachine1.txt";
    }
}
