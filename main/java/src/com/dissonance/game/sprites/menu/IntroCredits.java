package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class IntroCredits extends AbstractUI {
    private static final long FADE_TIME = 2000;
    private static final long STAY_UP = 3000;
    private static final long FADE_OUT_TIME = 1500;

    int hIndex, sIndex;
    int state = 0;
    long start;
    boolean continues = false;
    private static final String[] CREDIT_HEADER = new String[] {
            "Written by:",
            "Level Design by:",
            "Programmed by:",
            "Art by:",
            "A Cat in a Cup Game"
    };

    private static final String[] CREDIT_SUBTEXT = new String[] {
            "Henry Walker\nJohn Merrill",
            "AJ Goguen",
            "Eddie Penta\n" +
            "Alem Zupa\nheaddetect",
            "Thomas Gobeille\nDavis McVay\nRita Ding",
            ""
    };

    private TrueTypeFont headerFont;
    private TrueTypeFont subtextFont;
    @Override
    protected void onRender() {
        if (hIndex >= CREDIT_HEADER.length)
            return;
        RenderText.drawString(headerFont, CREDIT_HEADER[hIndex], getX(), getY(), new Color(1f, 1f, 1f, getAlpha()));
        if (!CREDIT_SUBTEXT[sIndex].equals("")) {
            String[] splitter = CREDIT_SUBTEXT[sIndex].split("\n");
            for (int i = 0; i < splitter.length; i++) {
                RenderText.drawString(subtextFont, splitter[i], getX(), (getY() + headerFont.getHeight()) + (subtextFont.getHeight() * i), new Color(1f, 1f, 1f, getAlpha()));
            }
        }
    }

    @Override
    protected void onOpen() {
        scale(false);
        headerFont = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(42f), Font.BOLD);
        subtextFont = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(24f), Font.PLAIN);

        setWidth(256);
        setHeight(128);

        marginTop(10f);
        marginLeft(240f);

        setAlpha(0f);
        start = RenderService.getTime();
    }

    @Override
    protected void onClose() { }

    public void continueIt() {
        state = 0;
        start = RenderService.getTime();
    }

    @Override
    public void update() {
        if (state == 0) {
            float alpha = Camera.ease(0f, 1f, FADE_TIME, (RenderService.getTime() - start));
            setAlpha(alpha);
            if (alpha >= 1f) {
                state = 1;
                start = RenderService.getTime();
            }
        } else if (state == 1) {
            if (RenderService.getTime() - start >= STAY_UP) {
                state = 2;
                start = RenderService.getTime();
            }
        } else if (state == 2) {
            float alpha = Camera.ease(1f, 0f, FADE_OUT_TIME, (RenderService.getTime() - start));
            setAlpha(alpha);
            if (alpha <= 0f) {
                state = (!continues ? 3 : 0);
                if (continues)
                    start = RenderService.getTime();
                hIndex++;
                sIndex++;
                if (sIndex >= CREDIT_SUBTEXT.length) {
                    close();
                }
            }
        }
    }

    public void alwaysContinue() {
        this.continues = true;
        continueIt();
    }
}
