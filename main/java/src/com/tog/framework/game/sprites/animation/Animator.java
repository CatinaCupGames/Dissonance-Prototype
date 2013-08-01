package com.tog.framework.game.sprites.animation;

public interface Animator {
    /**
     * This method is called after {@link com.tog.framework.game.sprites.animation.Animator#getSpeed()} frame(s) is
     * called. </br>
     * This method should advance the animation by 1 frame.
     */
    public void onAnimate();

    /**
     * The number of frames needed to pass in order for the next frame of the animation to play.
     * @return The number of frames
     */
    public int getSpeed();

    public void waitForAnimationEnd();

    public void wakeUp();

    public int getFrameCount();
}
