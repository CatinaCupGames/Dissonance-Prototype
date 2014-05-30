package com.dissonance.game.sprites.menu.buttons;

import com.dissonance.game.quests.MenuQuest;
import com.dissonance.game.sprites.menu.*;

public class PlayDemoButton extends TextButton {
    @Override
    protected void onClicked() {
        MenuQuest.INSTANCE.startGame();
    }

    @Override
    public String getText() {
        return "Start Demo";
    }

    @Override
    protected void onOpen() {
        super.onOpen();

        setX(640 - (getWidth() / 2f));
        setY((360 - (getHeight() + 40)));
    }
}
