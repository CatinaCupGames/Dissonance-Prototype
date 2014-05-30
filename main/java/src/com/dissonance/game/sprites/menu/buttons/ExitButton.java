package com.dissonance.game.sprites.menu.buttons;

import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.menu.TextButton;

public class ExitButton extends TextButton {
    @Override
    protected void onClicked() {
        RenderService.kill();
        System.exit(0);
    }

    @Override
    public String getText() {
        return "Quit";
    }

    @Override
    protected void onOpen() {
        super.onOpen();

        setX(640 - (getWidth() / 2f));
        setY((360 + (getHeight() + 40)));
    }
}
