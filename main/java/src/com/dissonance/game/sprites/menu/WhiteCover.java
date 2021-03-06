package com.dissonance.game.sprites.menu;

import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.game.quests.CatQuest;
import com.dissonance.game.sprites.ImageSprite;
import com.dissonance.game.w.menu.catworld;

import static org.lwjgl.opengl.GL11.*;

public class WhiteCover extends ImageSprite implements UpdatableDrawable {
    public WhiteCover() {
        super("sprites/menu/white.png");
    }

    private boolean start;
    private long startTime;

    @Override
    public void init() { }

    @Override
    public void update() {
        if (start) {
            float alpha = Camera.ease(0f, 1f, 2300L, RenderService.getTime() - startTime);
            setAlpha(alpha);
            if (alpha >= 1f) {
                start = false;
                Timer.delayedInvokeMethod(3000L, "fadeOut", CatQuest.INSTANCE);
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
        glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);
        super.render();
        glPopMatrix();
    }
}
