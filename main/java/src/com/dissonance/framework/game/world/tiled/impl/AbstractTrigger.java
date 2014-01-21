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

    public void onCollide(final PlayableSprite sprite) {
        if (System.currentTimeMillis() - lastTrigger < triggerTimeout() || !active) return;
        lastTrigger = System.currentTimeMillis();
        setActive(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    onTrigger(sprite);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                setActive(true);
            }
        }).start();
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

    protected abstract void onTrigger(PlayableSprite sprite) throws Throwable;

    protected abstract long triggerTimeout();
}
