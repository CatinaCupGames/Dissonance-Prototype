package com.dissonance.game.sprites.office;

import com.dissonance.game.sprites.ImagePhysicsSprite;
import com.dissonance.game.sprites.ImageSprite;

public class DeskChair extends ImagePhysicsSprite {
    public DeskChair() {
        super("sprites/img/officechair.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }
}
