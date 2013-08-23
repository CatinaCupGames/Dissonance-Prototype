package com.tog.framework.game.sprites.impl;

import com.tog.framework.render.Camera;

public abstract class PlayableSprite extends CombatSprite {
    private boolean isPlaying = false;
    private boolean frozen = false;
    private static PlayableSprite currentlyPlaying;

    @Override
    public void setX(float x) {
        super.setX(x);
        if (isPlaying)
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        if (isPlaying)
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
    }

    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Select this sprite to be the sprite the player will play as <br></br>
     * If the player is currently playing as another Sprite, then the {@link com.tog.framework.game.sprites.impl.PlayableSprite#deselect()} will be
     * invoke on that sprite. <br></br>
     *
     * The Camera will pan to the newly selected sprite
     */
    public void select() {
        if (currentlyPlaying != null)
            currentlyPlaying.deselect();

        currentlyPlaying = this;

        Camera.setCameraEaseListener(listener);
        Camera.easeMovement(Camera.translateToCameraCenter(getVector(), 32, 32), 800);
    }

    public void deselect() {
        isPlaying = false;
        currentlyPlaying = null;
        Camera.setCameraEaseListener(null); //Safety net
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public static PlayableSprite getCurrentlyPlayingSprite() {
        return currentlyPlaying;
    }

    public static void haltMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.frozen = true;
    }

    public static void resumeMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.frozen = false;
    }

    private final Camera.CameraEaseListener listener = new Camera.CameraEaseListener() {
        @Override
        public void onEase(float x, float y, long time) {
        }

        @Override
        public void onEaseFinished() {
            isPlaying = true;
            Camera.setCameraEaseListener(null); //Reset listener
        }
    };
}
