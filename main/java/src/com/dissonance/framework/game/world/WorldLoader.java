package com.dissonance.framework.game.world;

import com.dissonance.framework.system.annotations.OpenGLSafe;

public interface WorldLoader {

    /**
     * This method is invoked when a World is loaded into memory. When a World is loaded into memory, it may or may <b>NOT</b>
     * be displayed on the screen. <br></br>
     * @param world The World being loaded
     */
    public void onLoad(World world);

    /**
     * This method is invoked when a World is ready to be displayed on the screen to the player. When a World is ready to be displayed, it is assumed
     * the World has already been loaded into memory and the method {@link WorldLoader#onLoad(World)} was already invoked. <br></br>
     * @param world The World being displayed
     */
    public void onDisplay(World world);

}
