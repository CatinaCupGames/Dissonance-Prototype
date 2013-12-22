package com.dissonance.framework.game.combat.spells;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public abstract class StatusEffect {
    private static final long WAIT_PERIOD = 3000;
    private long lastInflict;
    protected long duration;
    protected long startTime;
    protected float damage;
    private boolean started = false;
    public StatusEffect(long duration, float damage) {
        this.duration = duration;
        this.damage = damage;
    }

    public boolean inflict(CombatSprite owner) {
        long cur = System.currentTimeMillis();
        if (cur - lastInflict > WAIT_PERIOD) {
            onInflict(owner);
            lastInflict = cur;
        }
        return cur - startTime > duration;
    }

    protected abstract void onInflict(CombatSprite owner);

    public boolean hasStarted() {
        return started;
    }

    public void startEffect() {
        started = true;
        startTime = System.currentTimeMillis();
        lastInflict = startTime;
    }
}
