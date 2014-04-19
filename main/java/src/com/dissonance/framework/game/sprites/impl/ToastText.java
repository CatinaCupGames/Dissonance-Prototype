package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class ToastText extends UpdatableSprite {
    private TrueTypeFont font;
    private Sprite parent;
    private String text;

    private float targetY;
    private long start;
    private float startY;
    private float duration;

    public ToastText(Sprite parent, String text, float duration) {
        super();
        this.parent = parent;
        this.text = text;

        startY = parent.getY();
        targetY = startY - 50f; //TODO Find a good value for this
        this.duration = duration;
    }

    @Override
    public void init() {
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(24f), Font.BOLD);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        start = RenderService.getTime();
    }

    boolean fadeOut = false;
    float alpha = 1f;
    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;

        setX(parent.getX());

        if (!fadeOut) {
            float target = Camera.ease(startY, targetY, duration, (RenderService.getTime() - start));
            setY(target);
            if (target == targetY) {
                fadeOut = true;
                start = RenderService.getTime(); //Reuse the start var
            }
        } else {
            alpha = Camera.ease(1f, 0f, 150f, (RenderService.getTime() - start));
        }
    }

    @Override
    public void render() {
        float r = getTint().getRed() / 255f, g = getTint().getGreen() / 255f, b = getTint().getBlue() / 255f;
        RenderText.drawString(font, text, getX(), getY(), new org.newdawn.slick.Color(r, g, b, alpha));
    }
}
