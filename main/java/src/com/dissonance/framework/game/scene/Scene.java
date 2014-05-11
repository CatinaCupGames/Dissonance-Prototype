package com.dissonance.framework.game.scene;

import com.dissonance.framework.system.annotations.OpenglUnsafe;

public interface Scene {

    /**
     * This method is invoked when the scene begins. <br></br>
     * This call <b>SHOULD ALWAYS BE</b> ran outside the render thread.
     */
    @OpenglUnsafe
    public void beginScene();

    /**
     * Terminate this scene. <br></br>
     * Invoking this method may not terminate the scene immediately. You can call {@link com.dissonance.framework.game.scene.Scene#waitForSceneEnd()} to wait for the end of
     * the scene.
     */
    public void terminateScene();

    /**
     * Returns whether or not the scene has started or not.
     * @return Returns true if the scene has started, otherwise false.
     */
    public boolean hasSceneStarted();

    /**
     * Wait for this scene to complete. This method will block code execution until the scene is either completed or this call
     * is interrupted. <br></br>
     * This call <b>SHOULD ALWAYS BE</b> ran outside the render thread.
     * @throws InterruptedException This can occur if this call was interrupted.
     */
    @OpenglUnsafe
    public void waitForSceneEnd() throws InterruptedException;
}
