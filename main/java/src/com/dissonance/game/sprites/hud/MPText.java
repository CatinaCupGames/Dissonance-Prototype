package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class MPText extends AbstractUI {
    private final BaseHUD baseHUD;
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

        marginLeft(5f);
        marginBottom(43f);
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() { }

    @Override
    public void render() {
        RenderText.drawString(font, "MP:" + (int)baseHUD.getMP() + "/" + (int)baseHUD.getMaxMP(), getX(), getY());
    }
}
