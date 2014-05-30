package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.menu.TextButton;
import com.dissonance.game.sprites.menu.pause.PauseMenu;

public class BackButton extends TextButton {
    @Override
    public String getText() {
        return "Back";
    }

    @Override
    protected void onClicked() {
        if (GameService.getCurrentQuest().isPaused()) {
            PauseMenu.INSTANCE.switchTo(PauseMenu.MAIN_MENU);
        } else if (GameService.getCurrentQuest() instanceof GameQuest) {
            ((GameQuest)GameService.getCurrentQuest()).closeTutorial();
        }
    }
}
