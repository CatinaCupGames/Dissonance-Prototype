package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.*;

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
        glPushMatrix();
        glLoadIdentity();
        glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);
        super.render();
        glPopMatrix();
        ShaderFactory.executePreRender();
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
