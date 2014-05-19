package com.dissonance.framework.game.combat.spells;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

/**
 * Status effects are effects that does something to a {@link com.dissonance.framework.game.sprites.impl.game.CombatSprite} over
 * a period of time. This period of time is defined in the constructor as <b>duration.</b> <br></br>
 */
public abstract class StatusEffect {
    private static final long WAIT_PERIOD = 3000;
    private long lastInflict;
    protected long duration;
    protected long startTime;
    protected double value;
    private boolean started = false;
    private boolean ended = false;

    /**
     * Create a new Status Effect.
     * @param duration How long this status effect will last, in ms.
     * @param value An arbitrary value that may or may not be used by the implementer.
     */
    public StatusEffect(long duration, double value) {

        this.duration = duration;
        this.value = value;
    }

    public boolean inflict(CombatSprite owner) {
        long cur = System.currentTimeMillis();
        if (cur - lastInflict > WAIT_PERIOD) {
            onInflict(owner);
            lastInflict = cur;
        }
        if (cur - startTime > duration) {
            ended = true;
            onEnd(owner);
            return true;
        }
        return false;
    }

    protected abstract void onStart(CombatSprite owner);

    protected abstract void onInflict(CombatSprite owner);

    protected abstract void onEnd(CombatSprite owner);

    public boolean hasStarted() {
        return started;
    }

    public void startEffect(CombatSprite owner) {
        started = true;
        startTime = System.currentTimeMillis();
        onStart(owner);
    }

    public boolean hasEnded() {
        return ended;
    }
}
