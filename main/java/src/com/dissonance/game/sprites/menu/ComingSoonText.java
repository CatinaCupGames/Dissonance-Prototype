package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.quests.BossQuest;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;


public class ComingSoonText extends AbstractUI {
    private static TrueTypeFont font;
    private static final long WAIT = 4000L;

    private long start;
    private boolean started = false;
    private boolean ended = false;
    private boolean stop = false;
    @Override
    protected void onRender() {
        Color color = new Color(Color.white);
        color.a = getAlpha();
        RenderText.drawString(font, "COMING SOON", getX(), getY(), color);
    }

    @Override
    protected void onOpen() {
        scale(false);

        if (font == null) {
            font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f).deriveFont(Font.BOLD), Font.BOLD);
        }

        setWidth(font.getWidth("COMING SOON"));
        setHeight(font.getHeight());

        setX(640 - (getWidth() / 2f));
        setY(310 + (getHeight() + 40) * 2);

        setAlpha(0f);
        start = System.currentTimeMillis();
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() {
        if (!started) {
            if (System.currentTimeMillis() - start > WAIT) {
                start = System.currentTimeMillis();
                started = true;
            }
        } else if (!ended) {
            float alpha = Camera.ease(0f, 1f, 1500f, System.currentTimeMillis() - start);
            setAlpha(alpha);
            if (alpha >= 1f) {
                ended = true;
                start = System.currentTimeMillis();
            }
        } else if (!stop) {
            if (System.currentTimeMillis() - start > 2000L) {
                stop = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BossQuest.INSTANCE.toMenu();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
