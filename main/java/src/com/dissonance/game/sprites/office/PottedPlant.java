package com.dissonance.game.sprites.office;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class PottedPlant extends ImagePhysicsSprite {
    public PottedPlant() {
        super("sprites/img/PottedPlant.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
