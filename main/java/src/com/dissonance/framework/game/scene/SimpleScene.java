package com.dissonance.framework.game.scene;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;

import java.util.Random;

/**
 * Represents a Simple Scene. <br></br>
 *
 * @see <a href="https://github.com/hypereddie/Dissonance/wiki/Programming%20Scenes#simple-scene">Simple Scenes in Wiki</a>
 */
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

    /**
     * Invoke {@link com.dissonance.framework.render.RenderService#fadeToBlack(float)}
     * @param speed The speed to fade at
     * @see com.dissonance.framework.render.RenderService#fadeToBlack(float)
     */
    protected void fadeToBlack(int speed) {
        RenderService.INSTANCE.fadeToBlack(speed);
    }

    /**
     * Invoke {@link com.dissonance.framework.render.RenderService#fadeFromBlack(float)}
     * @param speed The speed to fade at
     * @see com.dissonance.framework.render.RenderService#fadeFromBlack(float)
     */
    protected void fadeFromBlack(float speed) {
        RenderService.INSTANCE.fadeFromBlack(speed);
    }

    /**
     * Invoke {@link com.dissonance.framework.render.RenderService#waitForFade()}
     * @see com.dissonance.framework.render.RenderService#waitForFade()
     */
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

    /**
     * This method is invoked inside a new thread when {@link com.dissonance.framework.game.scene.Scene#beginScene()} is invoked.
     * @throws Throwable
     */
    protected abstract void playScene() throws Throwable;
}
