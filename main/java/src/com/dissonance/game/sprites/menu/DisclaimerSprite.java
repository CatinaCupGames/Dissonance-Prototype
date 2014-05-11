package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.settings.Color;
import org.newdawn.slick.*;

import java.awt.*;
import java.awt.Font;

public class DisclaimerSprite extends AbstractUI {
    private static final long FADE_TIME = 2000;
    private static final long STAY_UP = 3000;
    private static final long FADE_OUT_TIME = 1500;
    private static final String DISCLAIMER = "The following game is a tech demo for the game Dissonance.\n" +
                                             "Dissonance is under development, as such everything shown is\n" +
                                             "subject to change.";

    private TrueTypeFont font;
    private org.newdawn.slick.Color color = new org.newdawn.slick.Color(1f, 1f, 1f, 0f);
    @Override
    protected void onRender() {
        String[] lines = DISCLAIMER.split("\n");
        for (int i = 0; i < lines.length; i++) {
            RenderText.drawString(font, lines[i], getX(), getY() + (i * font.getHeight()), color);
        }
    }

    @Override
    protected void onOpen() {
        scale(false);
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(28f).deriveFont(Font.BOLD), Font.BOLD);

        setHeight(64);
        setWidth(256);
        marginLeft(120f);
        marginTop(280f);

        start = System.currentTimeMillis();
    }

    @Override
    protected void onClose() {

    }

    private int state = 0;
    private long start;
    @Override
    public void update() {
        if (state == 0) {
            color.a = Camera.ease(0f, 1f, FADE_TIME, System.currentTimeMillis() - start);
            if (color.a == 1f) {
                state++;
                start = System.currentTimeMillis();
            }
        } else if (state == 1) {
            if (System.currentTimeMillis() - start > STAY_UP) {
                state++;
                start = System.currentTimeMillis();
            }
        } else if (state == 2) {
            color.a = Camera.ease(1f, 0f, FADE_OUT_TIME, System.currentTimeMillis() - start);
            if (color.a == 0f) {
                state++;
                if (completeEvent != null)
                    completeEvent.run();
            }
        }
    }

    private Runnable completeEvent;
    public void setOnCompleteEvent(Runnable r) {
        this.completeEvent = r;
    }
}
