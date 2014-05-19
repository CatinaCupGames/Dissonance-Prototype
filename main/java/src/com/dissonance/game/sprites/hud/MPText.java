package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import sun.font.TextRecord;

import java.awt.*;
import java.awt.Font;

public class MPText extends AbstractUI {
    private final BaseHUD baseHUD;
    private Color color;
    public MPText(BaseHUD parent) {
        super(parent);
        baseHUD = parent;
    }

    TrueTypeFont font;
    @Override
    protected void onOpen() {
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f), Font.PLAIN);

        setWidth(font.getWidth("MP:9999/9999"));
        setHeight(font.getHeight("MP:9999/9999"));

        marginLeft(41f);
        marginBottom(23f);

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
        RenderText.drawString(font, "MP:" + (int)baseHUD.getMP() + "/" + (int)baseHUD.getMaxMP(), getX(), getY(), color);
    }
}
