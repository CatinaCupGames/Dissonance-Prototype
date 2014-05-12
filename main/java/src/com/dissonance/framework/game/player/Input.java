package com.dissonance.framework.game.player;

public interface Input {
    public static KeyboardInput KEYBOARD = new KeyboardInput();

    public void checkMovement(PlayableSprite playableSprite);

    public void checkKeys(PlayableSprite playableSprite);

    public boolean isMoving(PlayableSprite player);

    public String getName();

    public void update();
}
