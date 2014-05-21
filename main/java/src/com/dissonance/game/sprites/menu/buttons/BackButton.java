package com.dissonance.game.sprites.menu.buttons;

import com.dissonance.game.quests.MenuQuest;

public class BackButton extends TextButton {
    @Override
    protected void onClicked() {
        MenuQuest.INSTANCE.mainMenu();
    }

    @Override
    public String getText() {
        return "Back";
    }

    @Override
    protected void onOpen() {
        super.onOpen();

        setX(100);
        setY(500);
    }
}
