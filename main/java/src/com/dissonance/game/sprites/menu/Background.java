package com.dissonance.game.sprites.menu;

import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.*;

public class Background extends ImageSprite {
    public Background() {
        super("sprites/menu/Menus/Generic_Screen.png");
    }

    @Override
    public boolean neverClip() {
        return true;
    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);
        super.render();
        glPopMatrix();
    }
}