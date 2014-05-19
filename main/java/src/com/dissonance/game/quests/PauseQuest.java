package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.hud.BaseHUD;
import com.dissonance.game.sprites.menu.PauseMenu;
import com.dissonance.game.w.DemoLevelWorldLoader;

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
            for (BaseHUD hud : DemoLevelWorldLoader.huds) {
                if (hud != null)
                    hud.close();
            }

            Player[] players = Players.getPlayersWithInput();
            for (Player player : players) {
                BaseHUD hud = new BaseHUD(player);
                DemoLevelWorldLoader.huds[player.getNumber() - 1] = hud;
                hud.display(getWorld());

                if (player.getSprite() != null)
                    continue;
                player.join();
            }
        }
        GameService.coop_mode = Players.getCurrentlyPlayingSprites().length > 1;
    }
}
