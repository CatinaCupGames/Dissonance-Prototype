package com.dissonance.framework.game.player;

public interface Input {
    public void checkMovement(PlayableSprite playableSprite);

    public void checkKeys(PlayableSprite playableSprite);

    public String getName();
}
