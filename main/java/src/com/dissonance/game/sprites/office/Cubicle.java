package com.dissonance.game.sprites.office;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Cubicle extends ImagePhysicsSprite {
    public Cubicle() {
        super("sprites/img/Cubicle.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/cubicle_hitbox.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(45);
    }
}
