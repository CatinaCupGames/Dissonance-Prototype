package com.dissonance.game.sprites.office;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class Wall extends ImagePhysicsSprite {
    public Wall() {
        super("sprites/img/Wall.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/wall_hitbox.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setY(getY() - 1);
    }
}
