package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

public class StaminaBar extends AbstractUI {
    public StaminaBar(BaseHUD parent) {
        super(parent);
    }

    private static Texture texture;
    private double displayStamina = 100.0;
    private double actualStamina = 100.0;

    //Ease stuff
    private boolean ease;
    private double target;
    private double startH;
    private long start;

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    @Override
    protected void onOpen() {
        try {
            if (texture == null)
                texture = Texture.retrieveTexture("sprites/menu/player_hud/StaminaBar.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginLeft(50f);
            marginBottom(12f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _setDisplayStamina(100f);
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() {
        if (ease) {
            long since = RenderService.getTime() - start;
            float temp = Camera.ease((float) startH, (float) target, 500f, since);
            _setDisplayStamina(temp);
            if (temp == target)
                ease = false;
        }
    }

    public void setStamina(double actualStamina, double max) {
        double displayStamina = actualStamina / max;
        displayStamina *= 100.0;

        if (displayStamina < 0)
            displayStamina = 0;
        if (displayStamina > 100.0)
            displayStamina = 100.0;
        if (target == displayStamina && ease) return;

        ease = true;
        target = displayStamina;
        startH = this.displayStamina;
        start = RenderService.getTime();

        this.actualStamina = target;
    }

    private void _setDisplayStamina(float health) {
        this.displayStamina = health;
    }

    public double getStamina() {
        return actualStamina;
    }

    @Override
    public void onRender() {
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        double percent = bx * (-.02f * displayStamina + 2);

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3d(x + (bx - percent), y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3d(x + (bx - percent), y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
    }
}
