package com.dissonance.game.sprites.menu.pause;

import com.dissonance.game.sprites.menu.TextButton;

public class TutorialButton extends TextButton {
    @Override
    public String getText() {
        return "Tutorial";
    }

    @Override
    protected void onClicked() {
        PauseMenu.INSTANCE.switchTo(PauseMenu.TUTORIAL_MENU);
    }

    private static final int INDEX = 2;
    @Override
    public void onOpen() {
        super.onOpen();
        setX(640 - (getWidth() / 2f));
        setY(310 + ((getHeight() + 40) * (INDEX - 1)));
    }
}
