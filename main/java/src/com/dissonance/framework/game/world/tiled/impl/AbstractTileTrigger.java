package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.Tile;

public abstract class AbstractTileTrigger {
    private boolean active = true;
    private long lastTrigger = System.currentTimeMillis();

    public void onCollide(final AnimatedSprite sprite, final Tile obj) {
        if (System.currentTimeMillis() - lastTrigger < triggerTimeout() || !active) return;
        lastTrigger = System.currentTimeMillis();
        setActive(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    onTrigger(sprite, obj);
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

    public abstract void onTrigger(AnimatedSprite sprite, Tile tile);

    public abstract long triggerTimeout();
}
