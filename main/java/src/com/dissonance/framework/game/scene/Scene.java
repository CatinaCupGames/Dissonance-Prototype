package com.dissonance.framework.game.scene;

public interface Scene {

    public void beginScene();

    public void terminateScene();

    public boolean hasSceneStarted();

    public void waitForSceneEnd() throws InterruptedException;
}
