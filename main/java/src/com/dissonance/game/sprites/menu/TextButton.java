package com.dissonance.game.sprites.menu;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.quests.MenuQuest;
import com.dissonance.game.sprites.menu.Button;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public abstract class TextButton extends Button {
    @Override
    protected void normalRender() {
        COLOR.a = (227f / 255f) - (1f - RenderService.getCurrentAlphaValue());
        if (COLOR.a < 0f)
            COLOR.a = 0f;

        RenderText.drawString(text, getText(), getX(), getY(), COLOR);
    }

    @Override
    protected void activeRender() {
        COLOR.a = (227f / 255f) - (1f - RenderService.getCurrentAlphaValue());
        if (COLOR.a < 0f)
            COLOR.a = 0f;

        RenderText.drawString(activeText, getText(), getX() + (activeText.getWidth(getText()) / 32f), getY(), COLOR);
    }

    @Override
    protected void hoverRender() {
        normalRender();
    }

    private static TrueTypeFont text;
    private static TrueTypeFont activeText;
    private static final org.newdawn.slick.Color COLOR = new org.newdawn.slick.Color(1f, 1f, 1f, 227f / 255f);
    @Override
    protected void onOpen() {
        scale(false);
        if (text == null) {
            text = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f), Font.PLAIN);
        }
        if (activeText == null) {
            activeText = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(46f), Font.PLAIN);
        }

        setWidth(text.getWidth(getText()));
        setHeight(text.getHeight());
    }

    @Override
    protected void onClose() {

    }

    public abstract String getText();
}
