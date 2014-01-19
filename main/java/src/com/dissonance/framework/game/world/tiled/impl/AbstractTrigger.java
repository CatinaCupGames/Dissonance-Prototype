package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;

public abstract class AbstractTrigger {
    private boolean init = false;
    private boolean active = false;
    private long lastTrigger = System.currentTimeMillis();
    private TiledObject parent;

    public void init(TiledObject parent) {
        if (init)
            return;
        init = true;
        active = true;
        this.parent = parent;
    }

    public void onCollide(PlayableSprite sprite) {
        if (System.currentTimeMillis() - lastTrigger < triggerTimeout() || !active) return;
        lastTrigger = System.currentTimeMillis();
        onTrigger(sprite);
    }

    protected void setActive(boolean value) {
        this.active = value;
    }

    public boolean isActive() {
        return active;
    }

    protected TiledObject getParent() {
        return parent;
    }

    protected abstract void onTrigger(PlayableSprite sprite);

    protected abstract long triggerTimeout();
}
