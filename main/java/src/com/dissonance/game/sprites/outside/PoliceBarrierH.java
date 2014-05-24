package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;


public class PoliceBarrierH extends ImagePhysicsSprite {
    public PoliceBarrierH(){
        super("sprites/img/PoliceBarrierH.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/img/PoliceBarrierH.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (getLayer() == 0)
            setCutOffMargin(-80f);
    }
}
