package com.dissonance.game.sprites.menu.pause;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.GameCache;
import com.dissonance.game.quests.MenuQuest;
import com.dissonance.game.sprites.menu.Button;
import com.dissonance.game.sprites.menu.TextButton;

public class QuitButton extends TextButton {
    @Override
    public String getText() {
        return "Back to Menu";
    }

    @Override
    protected void onClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GameService.getCurrentQuest().resumeGame();
                GameService.getCurrentQuest().setNextQuest(new MenuQuest());
                try {
                    GameService.getCurrentQuest().endQuest();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static final int INDEX = 3;
    @Override
    public void onOpen() {
        super.onOpen();
        setX(640 - (getWidth() / 2f));
        setY(310 + (getHeight() + 40) * (INDEX - 1));
    }
}
