package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.*;

public class Overlay extends ImageSprite {
    private BackButton back;
    public Overlay() {
        super("sprites/img/pause.png");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        back = new BackButton();
        back.init();
        back.setX(1100);
        back.setY(640);

        setLayer(100);
    }

    @Override
     public void render() {
        ShaderFactory.executePostRender();
        back.update();
        glPushMatrix();
        glLoadIdentity();
        glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);
        super.render();
        back.render();
        glPopMatrix();
        ShaderFactory.executePreRender();
    }
}
