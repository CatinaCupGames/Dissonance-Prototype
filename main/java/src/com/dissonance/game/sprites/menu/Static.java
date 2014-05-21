package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.RenderService;

import static org.lwjgl.opengl.GL11.*;

public class Static extends AnimatedSprite {
    @Override
    public String getSpriteName() {
        return "static";
    }

    @Override
    public boolean neverClip() {
        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("static");
        playAnimation();
        setLayer(2);
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
