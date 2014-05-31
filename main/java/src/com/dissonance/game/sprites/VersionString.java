package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;

import java.awt.*;

public class VersionString extends AbstractUI {
    private static final String TEXT = "Engine Version: " + GameSettings.ENGINE_BUILD_STRING + "; Game Version: v1.0.0 RC1";
    private static TrueTypeFont font;
    @Override
    protected void onRender() {
        Color color = new Color(Color.white);
        color.a = RenderService.getCurrentAlphaValue();
        RenderText.drawString(font, TEXT, getX(), getY(), color);
    }

    @Override
    protected void onOpen() {
        scale(false);
        if (font == null) {
            font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(18f), Font.PLAIN);
        }

        setWidth(font.getWidth(TEXT));
        setHeight(font.getHeight());

        setX(30);
        marginBottom(10);
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() { }
}
