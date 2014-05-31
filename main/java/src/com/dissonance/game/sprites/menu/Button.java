package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.sprites.impl.UpdatableSprite;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.input.Mouse;

public abstract class Button extends AbstractUI {
    private boolean alive = true;
    private boolean hover;
    private boolean active;
    private boolean scale = true;

    @Override
    public void onRender() {
        if (hover && !active)
            hoverRender();
        else if (active)
            activeRender();
        else
            normalRender();
    }

    @Override
    public void update() {
        if (!alive)
            return;

        float mx = Mouse.getX();
        float my = Mouse.getY();
        my = GameSettings.Display.window_height - my;
        float[] translate = GameSettings.Display.toGameSpace(mx, my);
        mx = translate[0];
        my = translate[1];
        if (!hover) {
            if (mx >= getX() &&
                    mx <= getX() + getWidth() &&
                    my >= getY() &&
                    my <= getY() + getHeight()) {
                hover = true;
            }
        } else {
            if (mx < getX() ||
                    mx > getX() + getWidth() ||
                    my < getY() ||
                    my > getY() + getHeight()) {
                hover = false;
                return;
            }
            if (Mouse.isButtonDown(0) && !active) {
                active = true;
            } else if (!Mouse.isButtonDown(0) && active) {
                active = false;
                onClicked();
            }
        }
    }

    public void setActive(boolean value) {
        this.alive = value;
        if (!this.alive) {
            hover = false;
            active = false;
        }
    }

    public boolean isActive() {
        return alive;
    }

    protected abstract void onClicked();

    protected abstract void normalRender();

    protected abstract void activeRender();

    protected abstract void hoverRender();
}
