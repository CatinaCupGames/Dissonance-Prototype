package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;

import java.util.ArrayList;

public abstract class AbstractTrigger {
    private boolean init = false;
    protected ArrayList<PhysicsSprite> activators = new ArrayList<>();
    private long lastTrigger = System.currentTimeMillis();
    private TiledObject parent;

    public void init(TiledObject parent) {
        if (init)
            return;
        init = true;
        this.parent = parent;
    }

    public void onCollide(final PhysicsSprite sprite) {
        if (isPlayerOnly() && !(sprite instanceof PlayableSprite) || System.currentTimeMillis() - lastTrigger < triggerTimeout() || activators.contains(sprite)) return;
        lastTrigger = System.currentTimeMillis();
        activators.add(sprite);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    onTrigger(sprite);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                activators.remove(sprite);
            }
        }).start();
    }

    protected TiledObject getParent() {
        return parent;
    }

    protected abstract void onTrigger(PhysicsSprite sprite) throws Throwable;

    protected abstract long triggerTimeout();

    protected boolean isPlayerOnly() {
        return true;
    }
}
