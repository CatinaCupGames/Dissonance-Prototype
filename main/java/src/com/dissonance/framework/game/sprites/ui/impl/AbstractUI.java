package com.dissonance.framework.game.sprites.ui.impl;

import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Drawable;

public abstract class AbstractUI implements UI {
    protected float x, y;
    protected boolean opened;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public void display() {
        if (opened)
            return;
        WorldFactory.getCurrentWorld().loadAndAdd(this);
        opened = true;
    }

    public void close() {
        if (!opened)
            return;
        WorldFactory.getCurrentWorld().removeDrawable(this);
        opened = false;
        onClose();
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public void init() {
        onOpen();
    }

    @Override
    public int compareTo(Drawable d) {
        return Drawable.AFTER;
    }

    protected abstract void onOpen();

    protected abstract void onClose();

}
