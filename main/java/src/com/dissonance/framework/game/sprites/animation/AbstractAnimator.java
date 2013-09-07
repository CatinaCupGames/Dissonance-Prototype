package com.dissonance.framework.game.sprites.animation;

import com.dissonance.framework.render.RenderService;

public abstract class AbstractAnimator implements Animator {
    private int speed;

    @Override
    public int getAnimationSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public synchronized final void waitForAnimationEnd() {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
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
