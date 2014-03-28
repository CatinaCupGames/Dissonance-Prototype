package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class HealthTip extends AbstractUI {
    public HealthTip(HealthBar parent) {
        super(parent);
    }

    private static Texture texture;
    private boolean visible = true;
    @Override
    protected void onOpen() {
        try {
            if (texture == null)
                texture = Texture.retriveTexture("sprites/menu/player_hud/htip.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            centerVertical();
            marginRight(109f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() {

    }

    private float otemp = 0;
    @Override
    public void render() {
        super.render();
        if (!visible)
            return;
        if (texture == null)
            return;

        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
        super.resetAlpha();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
