package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.UpdatableDrawable;

public abstract class UpdatableSprite extends Sprite implements UpdatableDrawable {
    private boolean dontUpdate;

    protected boolean isUpdateCanceled() {
        return dontUpdate;
    }

    protected void setUpdateCanceled(boolean value) {
        this.dontUpdate = value;
    }

    @Override
    public void update() {
        this.dontUpdate = false;
    }
}
