package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.game.spells.statuseffects.*;
import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;

import javax.swing.plaf.BorderUIResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public class StatusEffectBar extends AbstractUI {
    private static boolean done = false;
    private static Texture texture;

    private ArrayList<StatusEffect> effects = new ArrayList<>();
    private ArrayList<StatusEffectImage> images = new ArrayList<>();

    private static final long ANIMATE_DURATION = 1400L;
    private static final float OPEN_MARGIN = 5f;
    private static final float CLOSE_MARGIN = 18f;
    private boolean animate;
    private boolean lockCall = false;
    private float start, end;
    private long startTime;

    private boolean opened = false;
    private boolean opening = false;
    private boolean closing = false;

    private float bMargin;

    public StatusEffectBar(AbstractUI parent) {
        super(parent);
    }

    @Override
    protected void onRender() {
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();

        float start = x - bx;
        start += 18f;
        y = getY() - 2f;
        for (int i = 0; i < images.size(); i++) {
            images.get(i).render(start + (i * 14f), y);
        }
    }

    @Override
    protected void onOpen() {
        if (!done) {
            for (StatusEffectImage img : StatusEffectImage.values()) {
                img.setup();
            }
            done = true;
        }

        if (texture == null) {
            try {
                texture = Texture.retrieveTexture("sprites/menu/player_hud/StatusEffectBar.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setWidth(texture.getTextureWidth());
        setHeight(texture.getTextureHeight());

        alignToTexture(texture);

        marginLeft(48f);
        marginBottom(CLOSE_MARGIN);
        bringToBack();
    }

    @Override
    public void marginBottom(float value) {
        super.marginBottom(value);
        this.bMargin = value;
    }


    @Override
    protected void onClose() {

    }

    public void addEffect(StatusEffect effect) {
        if (effects.size() == 0) {
            animateOpen();
        }
        effects.add(effect);
        populateImages();
    }

    public boolean hasEffect(StatusEffect effect) {
        return effects.contains(effect);
    }

    @Override
    public void update() {
        lockCall = false;

        if (animate) {
            float value = Camera.ease(start, end, ANIMATE_DURATION, (RenderService.getTime() - startTime));
            marginBottom(value);
            if (value == end) {
                animate = false;

                opened = opening;
            }
        }

        if (opened) {
            Iterator<StatusEffect> effects = this.effects.iterator();
            while (effects.hasNext()) {
                StatusEffect effect = effects.next();
                if (effect.hasEnded()) {
                    effects.remove();
                    populateImages();
                }
            }

            if (this.effects.size() == 0 && !animate)
                animateClose();
        }
    }

    private void animateOpen() {
        animate = true;

        start = bMargin;
        end = OPEN_MARGIN;
        startTime = RenderService.getTime();

        closing = false;
        opening = true;

        lockCall = true;
    }

    private void animateClose() {
        animate = true;

        start = bMargin;
        end = CLOSE_MARGIN;
        startTime = RenderService.getTime();

        closing = true;
        opening = false;

        lockCall = true;
    }

    private void populateImages() {
        images.clear();
        for (StatusEffect e : effects) {
            StatusEffectImage img = StatusEffectImage.convert(e);
            if (images.contains(img)) {
                img.alpha += 0.1f;
            } else {
                img.alpha = 0.5f;
                images.add(img);
            }
        }
    }

    public enum StatusEffectImage {
        BLEED("sprites/menu/player_hud/BleedIcon.png"),
        FIRE("sprites/menu/player_hud/FireIcon.png"),
        HP("sprites/menu/player_hud/HPhealIcon.png"),
        MP("sprites/menu/player_hud/MPhealIcon.png"),
        STATBOOST("sprites/menu/player_hud/StatupIcon.png"),
        STUN("sprites/menu/player_hud/StunIcon.png"),
        NONE("");

        private Texture texture;
        private String image;
        private float alpha;
        StatusEffectImage(String image) {
            this.image = image;
        }

        public Texture getTexture() {
            return texture;
        }

        public void render(float x, float y) {
            if (texture == null)
                return;
            float bx = texture.getTextureWidth() / 2f, by = texture.getTextureHeight() / 2f, z = 0;

            float fAlpha = RenderService.getCurrentAlphaValue() - (1f - alpha);
            if (fAlpha < 0f)
                fAlpha = 0f;

            glColor4f(1f, 1f, 1f, fAlpha);
            texture.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0f, 0f); //bottom left
            glVertex3f(x - bx, y - by, z);
            glTexCoord2f(1f, 0f); //bottom right
            glVertex3f(x + bx, y - by, z);
            glTexCoord2f(1f, 1f); //top right
            glVertex3f(x + bx, y + by, z);
            glTexCoord2f(0f, 1f); //top left
            glVertex3f(x - bx, y + by, z);
            glEnd();
            texture.unbind();
            glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        public float getAlpha() {
            return alpha;
        }

        private void setup() {
            if (this == NONE)
                return;
            try {
                texture = Texture.retrieveTexture(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static StatusEffectImage convert(StatusEffect effect) {
            if (effect instanceof Burn)
                return FIRE;
            else if (effect instanceof HPHeal)
                return HP;
            else if (effect instanceof MPHeal)
                return MP;
            else if (effect instanceof StatBuff)
                return STATBOOST;
            else if (effect instanceof Stun)
                return STUN;
            else if (effect instanceof Wound)
                return BLEED;
            else
                return NONE;
        }
    }
}
