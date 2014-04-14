package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class BaseHUD extends AbstractUI {
    private static Texture texture;

    private HealthBar healthBar;
    private MPBar mpBar;
    private LevelText levelText;
    private HealthText healthText;
    private MPText mpText;

    public BaseHUD() {
        super();
        healthBar = new HealthBar(this);
        mpBar = new MPBar(this);
        levelText = new LevelText(this);
        healthText = new HealthText(this);
        mpText = new MPText(this);
    }
    @Override
    protected void onOpen() {
        scale(false);
        try {
            if (texture == null)
                texture = Texture.retriveTexture("sprites/menu/player_hud/base.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginBottom(-8f);
            marginLeft(8f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (healthBar.isOpened())
            healthBar.close();
        if (mpBar.isOpened())
            mpBar.close();
        if (levelText.isOpened())
            levelText.close();
        if (healthText.isOpened())
            healthText.close();
        if (mpText.isOpened())
            mpText.close();

        healthBar.display(world);
        mpBar.display(world);
        mpText.display(world);
        healthText.display(world);
        levelText.display(world);
        levelText.scale(false);
        healthText.scale(false);
        mpText.scale(false);
        mpBar.scale(false);
        healthBar.scale(false);
    }

    @Override
    protected void onClose() {
    }

    private boolean overIt = true;
    private boolean ease;
    private long startT;
    private float target;
    private float start;
    @Override
    public void update() {
        float minX = 0f, maxX = 480f / 2f, minY = 0, maxY = 270f / 2f;
        float mx = Mouse.getX(), my = Mouse.getY();

        if (mx > minX && mx < maxX && my > minY && my < maxY) {
            if (!overIt) {
                overIt = true;
                ease = true;
                target = 1f;
                start = getAlpha();
                startT = RenderService.getTime();
            }
        } else {
            if (overIt) {
                overIt = false;
                ease = true;
                target = 0.5f;
                start = getAlpha();
                startT = RenderService.getTime();
            }
        }
        if (ease) {
            long since = RenderService.getTime() - startT;
            float temp = Camera.ease(start, target, 250f, since);
            if (temp == target)
                ease = false;
            setAlpha(temp);
            healthBar.setAlpha(temp);
            mpBar.setAlpha(temp);
            mpText.setAlpha(temp);
            healthText.setAlpha(temp);
            levelText.setAlpha(temp);
        }
    }

    public void setHealth(float health) {
        healthBar.setHealth(health);
    }

    public void setMP(float MP) {
        mpBar.setMP(MP);
    }

    public void setLevel(int level) {
        levelText.setLevel(level);
    }

    public float getHealth() {
        return healthBar.getHealth();
    }

    public float getMP() {
        return mpBar.getMP();
    }

    public float getLevel() {
        return levelText.getLevel();
    }

    @Override
    public void onRender() {
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

    public float getMaxMP() {
        return mpBar.getMaxMP();
    }

    public void setMaxMP(float mp) {
        mpBar.setMaxMP(mp);
    }
}
