package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.menu.PauseMenu;

public abstract class PauseQuest extends AbstractQuest {
    private PauseMenu menu = new PauseMenu();
    @Override
    public void onPauseGame() {
        menu.display(getWorld());
        RenderService.INSTANCE.provideData(true, RenderService.DONT_UPDATE);
    }

    @Override
    public void onResumeGame() {
        menu.reset();
        menu.close();
        RenderService.INSTANCE.provideData(false, RenderService.DONT_UPDATE);
    }
}
