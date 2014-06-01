package com.dissonance.game.sprites.environment;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.*;

public class BasicLight extends ImageSprite implements UI {
    public BasicLight() {
        super("sprites/img/lightglow.png");
    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef(-Camera.getX(), -Camera.getY(), 0f);
        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());

        super.render();

        glPopMatrix();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setLayer(100);
    }

    @Override
    public void init() { }

    @Override
    public void update() { }

    @Override
    public void close() {

    }
}
