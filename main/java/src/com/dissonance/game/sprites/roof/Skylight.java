package com.dissonance.game.sprites.roof;

import com.dissonance.game.sprites.ImageSprite;

public class Skylight extends ImageSprite {
    public Skylight(){super("sprites/img/skylight.png");}

    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-80);
    }
}
