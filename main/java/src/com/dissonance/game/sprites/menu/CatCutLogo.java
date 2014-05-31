package com.dissonance.game.sprites.menu;

import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.game.sprites.ImageSprite;
import com.dissonance.game.w.menu.catworld;

import static org.lwjgl.opengl.GL11.*;

public class CatCutLogo extends ImageSprite implements UpdatableDrawable {
    public CatCutLogo() {
        super("sprites/menu/catcuplogo.png");
    }

    private boolean start;
    private long startTime;

    @Override
    public void init() { }

    @Override
    public void update() {
        if (start) {
            float alpha = Camera.ease(0f, 1f, 1300L, RenderService.getTime() - startTime);
            setAlpha(alpha);
            if (alpha >= 1f) {
                start = false;
                Timer.delayedInvokeMethod(1300, "start", catworld.cover);
            }
        }
    }

    public void start() {
        start = true;
        startTime = RenderService.getTime();
    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        super.render();
        glPopMatrix();
    }
}
