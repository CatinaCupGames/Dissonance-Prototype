package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building2 extends ImagePhysicsSprite {
    public Building2() {
        super("sprites/buildings/building2.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building2.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-60);
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
