package com.dissonance.game.sprites.menu.pause;

import com.dissonance.framework.render.texture.Texture;
import com.dissonance.game.sprites.menu.TextButton;

public class BackButton extends TextButton {
    @Override
    public String getText() {
        return "Back";
    }

    @Override
    protected void onClicked() {
        PauseMenu.INSTANCE.switchTo(PauseMenu.MAIN_MENU);
    }
}
