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

    private long start;
    private float duration;
    private final float target;

    public ToastText(Sprite parent, String text, float duration) {
        super();
        this.parent = parent;
        this.text = text;
        this.duration = duration;

        target = (parent.getHeight() / 2f) + 9f;
    }

    public Sprite getToastParent() {
        return parent;
    }

    public String getToastString() {
        return text;
    }

    public float getToastDuration() {
        return duration;
    }

    private float fSize = 24f;
    @Override
    public void init() {
        font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(fSize), Font.BOLD);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        start = RenderService.getTime();
        setLayer(2);
    }

    @Override
    public float getWidth() {
        return font.getWidth(text);
    }

    @Override
    public float getHeight() {
        return font.getHeight(text);
    }

    public float getToastFontSize() {
        return fSize;
    }

    public ToastText setToastFontSize(float size) {
        this.fSize = size;
        init(); //Recreate font object
        return this;
    }

    boolean fadeOut = false;
    float alpha = 0f;
    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;

        if (!fadeOut) {
            setX(parent.getX() - (getWidth() / 4f));
            setX(getX() * 2f);

            float targetAlpha = Camera.ease(0f, 1f, (duration - 200), (RenderService.getTime() - (start + 100L)));
            float target = Camera.ease(0f, this.target, duration, (RenderService.getTime() - (start + 100L)));
            setY(parent.getY() - target);
            setY(getY() * 2f);
            alpha = targetAlpha;
            if (target == this.target) {
                fadeOut = true;
                alpha = 1f;
                start = RenderService.getTime(); //Reuse the start var
            }
        } else {
            alpha = Camera.ease(1f, 0f, 350f, (RenderService.getTime() - start));
        }
    }

    @Override
    public void render() {
        RenderService.removeScale();
        float r = getTint().getRed() / 255f, g = getTint().getGreen() / 255f, b = getTint().getBlue() / 255f;
        RenderText.drawString(font, text, getX(), getY(), new org.newdawn.slick.Color(r, g, b, alpha));
        RenderService.resetScale();
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
