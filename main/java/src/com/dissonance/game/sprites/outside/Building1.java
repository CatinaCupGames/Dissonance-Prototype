package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building1 extends ImagePhysicsSprite {
    public Building1() {
        super("sprites/buildings/building1.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building1.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-80);
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
