package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building3 extends ImagePhysicsSprite {
    public Building3() {
        super("sprites/buildings/building3.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building3.txt";
    }
    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-10f);
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
