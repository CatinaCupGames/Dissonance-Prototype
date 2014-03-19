package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class HealthBar extends AbstractUI {
    public HealthBar(BaseHUD parent) {
        super(parent);
        tip = new HealthTip(this);
    }

    private static Texture texture;
    private float displayHealth = 100f;
    private float actualHealth = 100f;
    private HealthTip tip;

    //Ease stuff
    private boolean ease;
    private float target;
    private float startH;
    private long start;

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        tip.setAlpha(alpha);
    }

    @Override
    protected void onOpen() {
        try {
            if (texture == null)
                texture = Texture.retriveTexture("sprites/menu/player_hud/hbar.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginLeft(73f);
            marginBottom(80f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tip.isOpened())
            tip.close();

        tip.display(world);
        _setDisplayHealth(100f);
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() {
        if (ease) {
            long since = RenderService.getTime() - start;
            float temp = Camera.ease(startH, target, 500f, since);
            _setDisplayHealth(temp);
            if (temp == target)
                ease = false;
        }
    }

    public void setHealth(float displayHealth) {
        if (displayHealth < 0)
            displayHealth = 0;
        if (displayHealth > 100)
            displayHealth = 100;
        if (target == displayHealth && ease) return;

        ease = true;
        target = displayHealth;
        startH = this.displayHealth;
        start = RenderService.getTime();

        this.actualHealth = target;
    }

    private void _setDisplayHealth(float health) {
        this.displayHealth = health;
        if (health >= 100 || health <= 0)
            tip.setVisible(false);
        else {
            tip.setVisible(true);
            tip.marginRight(-0.0004385153f * (health * health) + -1.195671444f * health + 127.0015803f);
        }
    }

    public float getHealth() {
        return actualHealth;
    }

    @Override
    public void render() {
        super.render();
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        float percent = bx * (-.02f * displayHealth + 2);

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + (bx - percent), y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + (bx - percent), y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
        super.resetAlpha();
    }
}
