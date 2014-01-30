package com.dissonance.framework.game.scene;

import com.dissonance.framework.render.RenderService;

public abstract class SimpleScene implements Scene {
    private boolean scenePlaying = false;
    @Override
    public void beginScene() {
        scenePlaying = true;
        playScene();
        scenePlaying = false;
        _wakeup();
    }

    @Override
    public void terminateScene() {
        throw new RuntimeException("Can't terminate a simple scene!");
    }

    private synchronized void _wakeup() {
        super.notifyAll();
    }

    @Override
    public boolean hasSceneStarted() {
        return scenePlaying;
    }

    @Override
    public synchronized void waitForSceneEnd() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new RuntimeException("Can't block the render thread!");
        while (true) {
            if (!scenePlaying)
                 break;
            super.wait(0L);
        }
    }

    protected abstract void playScene();
}
