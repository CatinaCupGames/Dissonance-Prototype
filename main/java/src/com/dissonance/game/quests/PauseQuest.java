package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.menu.PauseMenu;

public abstract class PauseQuest extends AbstractQuest {
    private PauseMenu menu = new PauseMenu();
    private int oplayer;
    @Override
    public void onPauseGame() {
        oplayer = Players.getPlayingCount();
        menu.display(getWorld());
        RenderService.INSTANCE.provideData(true, RenderService.DONT_UPDATE);
    }

    @Override
    public void onResumeGame() {
        menu.reset();
        menu.close();
        RenderService.INSTANCE.provideData(false, RenderService.DONT_UPDATE);
        if (oplayer != Players.getPlayersWithInput().length) {
            Player[] players = Players.getPlayersWithInput();
            for (Player player : players) {
                if (player.getSprite() != null)
                    continue;
                player.join();
            }
        }
    }
}
