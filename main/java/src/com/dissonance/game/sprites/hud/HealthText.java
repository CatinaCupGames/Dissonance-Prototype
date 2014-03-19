package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.opengl.NVDrawTexture;
import org.newdawn.slick.*;

import java.awt.*;
import java.awt.Font;

public class HealthText extends AbstractUI {
    private final BaseHUD baseHUD;
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

        marginLeft(15f);
        marginBottom(57f);
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() { }

    @Override
    public void render() {
        super.render();
        font.drawString(getX(), getY(), "HP:" + (int)baseHUD.getHealth() + "/" + 100, new org.newdawn.slick.Color(1f, 1f, 1f, getAlpha()));
        super.resetAlpha();
    }
}
