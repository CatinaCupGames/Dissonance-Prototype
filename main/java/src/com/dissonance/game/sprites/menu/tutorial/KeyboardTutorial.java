package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.game.sprites.ImageSprite;

public class KeyboardTutorial extends ImageSprite {
    public KeyboardTutorial() {
        super("sprites/menu/Menus/KeyboardTutorial.png");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setLayer(101);
    }

    @Override
    public void render() {
        ShaderFactory.executePostRender();
        super.render();
        ShaderFactory.executePreRender();
    }
}
