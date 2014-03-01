package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.TextureLoader;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class BaseHUD extends AbstractUI {
    private Texture texture;
    private float height_difference;

    private HealthBar healthBar;
    private MPBar mpBar;

    public BaseHUD() {
        super();
        healthBar = new HealthBar(this);
        mpBar = new MPBar(this);
    }
    @Override
    protected void onOpen() {
        try {
            texture = Texture.retriveTexture("sprites/menu/player_hud/base.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginBottom(8f);
            marginLeft(8f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (healthBar.isOpened())
            healthBar.close();
        if (mpBar.isOpened())
            mpBar.close();

        healthBar.display(world);
        mpBar.display(world);
    }

    public HealthBar getHealthBarUI() {
        return healthBar;
    }

    @Override
    protected void onClose() {
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
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
    }
}
