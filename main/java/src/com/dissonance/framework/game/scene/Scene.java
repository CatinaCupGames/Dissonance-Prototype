package com.dissonance.framework.game.scene;

import com.dissonance.framework.system.annotations.OpenglUnsafe;

public interface Scene {

    @OpenglUnsafe
    public void beginScene();

    public void terminateScene();

    public boolean hasSceneStarted();

    @OpenglUnsafe
    public void waitForSceneEnd() throws InterruptedException;
}
