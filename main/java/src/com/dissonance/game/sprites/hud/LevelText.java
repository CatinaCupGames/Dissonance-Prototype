package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import java.awt.*;
import java.awt.Font;
import java.lang.reflect.Field;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class LevelText extends AbstractUI {
    public LevelText(BaseHUD hud) {
        super(hud);
    }

    TrueTypeFont font;
    int level = 1;
    @Override
    protected void onOpen() {
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f), Font.PLAIN);

        setWidth(font.getWidth("99"));
        setHeight(font.getHeight("99"));

        marginLeft(3f);
        marginTop(-15f);
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() { }

    public void setLevel(int level) {
        if (level <= 0)
            level = 1;
        if (level > 99)
            level = 99;

        this.level = level;
    }

    @Override
    public void onRender() {
        float x = level < 10 ? getX() + (font.getWidth("" + level) / 2f) : getX();
        if (level == 1)
            x += font.getWidth("1") / 1.5f;
        if (level >= 10 && level < 20)
            x += font.getWidth("" + level) / (level == 11 ? 4f : 8f);
        RenderText.drawString(font, "" + level, x, getY(), new Color(1f, 1f, 1f, getAlpha()));
        //font.drawString(x, getY(), "" + level, new Color(1f, 1f, 1f, getAlpha()));
    }

    public int getLevel() {
        return level;
    }
}
