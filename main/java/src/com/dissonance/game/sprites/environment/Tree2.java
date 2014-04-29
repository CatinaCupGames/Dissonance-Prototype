package com.dissonance.game.sprites.environment;

import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.game.sprites.ImageSprite;

public class Tree2 extends ImageSprite {
    public Tree2() {
        super("sprites/img/tree2.png");
    }

    @Override
    public void onLoad() {
        boolean old = TextureLoader.isFastRedraw();
        TextureLoader.setFastRedraw(false);
        super.onLoad();
        TextureLoader.setFastRedraw(old);
    }
}
