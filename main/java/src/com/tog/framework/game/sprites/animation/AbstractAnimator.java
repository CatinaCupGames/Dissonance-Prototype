package com.tog.framework.game.sprites.animation;

public abstract class AbstractAnimator implements Animator {
    private int speed;

    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public synchronized final void waitForAnimationEnd() {
        try {
            super.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized final void wakeUp() {
        super.notify();
    }
}
