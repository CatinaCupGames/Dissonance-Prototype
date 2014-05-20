package com.dissonance.game.sprites;

/**
 * Created by john on 5/14/14.
 */
public class StreetLamp extends ImagePhysicsSprite {
    public StreetLamp() {
        super("sprites/img/streetlamp.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/streetlamp.txt";
    }
}
