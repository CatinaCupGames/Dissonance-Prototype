package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.opengl.NVDrawTexture;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import sun.java2d.pipe.TextRenderer;

import java.awt.*;
import java.awt.Font;

public class HealthText extends AbstractUI {
    private final BaseHUD baseHUD;
    private Color color;
    public HealthText(BaseHUD parent) {
        super(parent);
        baseHUD = parent;
    }

    TrueTypeFont font;
    @Override
    protected void onOpen() {
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f), Font.PLAIN);

        setWidth(font.getWidth("HP:100/100"));
        setHeight(font.getHeight("HP:100/100"));

        marginLeft(75f);
        marginBottom(35f);

        setAlpha(1f); //Set the color
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() { }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        float color_alpha = alpha - 0.3f;
        if (color_alpha < 0f)
            color_alpha = 0f;
        this.color = new Color(1f, 1f, 1f, color_alpha);
    }

    @Override
    public void onRender() {
        RenderText.drawString(font, "HP:" + (int)baseHUD.getHealth() + "/" + 100, getX(), getY(), color);
    }
}
