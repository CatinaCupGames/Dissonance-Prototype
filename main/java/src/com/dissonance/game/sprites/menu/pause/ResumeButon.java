package com.dissonance.game.sprites.menu.pause;

import com.dissonance.framework.game.GameService;
import com.dissonance.game.sprites.menu.TextButton;

public class ResumeButon extends TextButton {
    @Override
    public String getText() {
        return "Resume Game";
    }

    @Override
    protected void onClicked() {
        GameService.getCurrentQuest().resumeGame();
    }

    private static final int INDEX = 0;
    @Override
    public void onOpen() {
        super.onOpen();
        setX(640 - (getWidth() / 2f));
        setY(310 + ((getHeight() + 40) * (INDEX - 1)));
    }
}
