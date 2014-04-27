package com.dissonance.framework.system.debug;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;

import java.awt.*;
import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

public class DebugSprite extends AbstractUI {


    @Override
    protected void onRender() {
        float x = getX();
        float y = getY();
        float bx = getWidth() / 2f;
        float by = getHeight() / 2f;
        float z = 0f;

        glColor4f(0.1f, 0.1f, 0.1f, 0.7f);
        glBegin(GL_QUADS);
        glVertex3f(x - bx, y - by, z);
        glVertex3f(x + bx, y - by, z);
        glVertex3f(x + bx, y + by, z);
        glVertex3f(x - bx, y + by, z);
        glEnd();

        RenderText.drawString(bold_font, "Debug Panel:", (getX() - bx) + 10f, (getY() - by) + 5f, new Color(1f, 1f, 1f));
        RenderText.drawString(font, "FPS: " + RenderService.FPS, (getX() - bx) + 10f, ((getY() - by) + bold_font.getHeight()) + 10f, new Color(1f, 1f, 1f));
        RenderText.drawString(font, "Sprites: " + super.world.getDrawableCount(), (getX() - bx) + 10f, (((getY() - by) + bold_font.getHeight()) + font.getHeight()) + 10f, new Color(1f, 1f, 1f));
        RenderText.drawString(font, "USprites: " + super.world.getUpdatableCount(), (getX() - bx) + 10f, (((getY() - by) + bold_font.getHeight()) + font.getHeight() * 2f) + 10f, new Color(1f, 1f, 1f));
        DecimalFormat format = new DecimalFormat("#.0000");
        RenderText.drawString(font, "Memory%: " + format.format(Debug.getPercentUsed()) + "%", (getX() - bx) + 10f, (((getY() - by) + bold_font.getHeight()) + font.getHeight() * 3f) + 10f, new Color(1f, 1f, 1f));
        RenderText.drawString(font, "Memory Free: " + format.format(Debug.getFreeMemory() * 9.53674e-7) + "mb", (getX() - bx) + 10f, (((getY() - by) + bold_font.getHeight()) + font.getHeight() * 4f) + 10f, new Color(1f, 1f, 1f));

        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());


    }

    TrueTypeFont font;
    TrueTypeFont bold_font;
    @Override
    protected void onOpen() {
        scale(false);
        bold_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f), Font.BOLD);
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(24f), Font.PLAIN);

        setWidth(256);
        setHeight(200);

        marginTop(10f);
        marginLeft(10f);
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() {

    }
}
