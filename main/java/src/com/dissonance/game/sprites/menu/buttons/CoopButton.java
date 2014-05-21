package com.dissonance.game.sprites.menu.buttons;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.quests.MenuQuest;
import com.dissonance.game.sprites.menu.Button;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class CoopButton extends TextButton {
    @Override
    protected void onClicked() {
        MenuQuest.INSTANCE.coopMenu();
    }

    @Override
    public String getText() {
        return "Start Co-Op";
    }

    @Override
    protected void onOpen() {
        super.onOpen();

        setX(640 - (getWidth() / 2f));
        setY(360);
    }
}
