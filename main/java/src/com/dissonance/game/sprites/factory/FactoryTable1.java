package com.dissonance.game.sprites.factory;

import com.dissonance.game.sprites.ImagePhysicsSprite;

/**
 * Created by Jmerrill on 5/21/2014.
 */
public class FactoryTable1 extends ImagePhysicsSprite {
    public FactoryTable1() {
        super("sprites/img/FactoryTable1.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/FactoryTable1.txt";
    }
}
