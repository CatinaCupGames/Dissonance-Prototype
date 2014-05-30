package com.dissonance.game.sprites.menu.pause;

import com.dissonance.game.sprites.menu.TextButton;

public class CoopButton extends TextButton {
    @Override
    public String getText() {
        return "Co-op Setup";
    }

    @Override
    protected void onClicked() {
        PauseMenu.INSTANCE.switchTo(PauseMenu.COOP_MENU);
    }

    private static final int INDEX = 1;
    @Override
    public void onOpen() {
        super.onOpen();
        setX(640 - (getWidth() / 2f));
        setY(310 + ((getHeight() + 40) * (INDEX - 1)));
    }
}
