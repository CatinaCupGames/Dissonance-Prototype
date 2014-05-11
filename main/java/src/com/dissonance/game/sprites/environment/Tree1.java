package com.dissonance.game.sprites.environment;

import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.game.sprites.ImageSprite;

public class Tree1 extends ImageSprite {
    public Tree1() {
        super("sprites/img/tree1.png");
    }

    @Override
    public void onLoad() {
        boolean old = TextureLoader.isFastRedraw();
        TextureLoader.setFastRedraw(false);
        super.onLoad();
        TextureLoader.setFastRedraw(old);
    }
}
