package com.dissonance.framework.game.scene;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;

import java.util.Random;

public abstract class SimpleScene implements Scene {
    private boolean scenePlaying = false;
    @Override
    public void beginScene() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("==SCENE " + new Random().nextLong() + "==");
                scenePlaying = true;
                try {
                    playScene();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                scenePlaying = false;
                _wakeup();
            }
        }).start();
    }

    protected World getWorld() {
        return GameService.getCurrentWorld();
    }

    protected void fadeToBlack(int speed) {
        RenderService.INSTANCE.fadeToBlack(speed);
    }

    protected void fadeFromBlack(int speed) {
        RenderService.INSTANCE.fadeFromBlack(speed);
    }

    protected void waitForFade() throws InterruptedException {
        RenderService.INSTANCE.waitForFade();
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
        while (true) {
            if (!scenePlaying)
                 break;
            super.wait(0L);
        }
    }

    protected abstract void playScene() throws Throwable;
}
