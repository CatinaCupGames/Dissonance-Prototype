package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;


public class PoliceBarrierV extends ImagePhysicsSprite {
    public PoliceBarrierV(){
        super("sprites/img/PoliceBarrierV.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/PoliceBarrierV.txt";
    }
}
