package com.dissonance.game.quests;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.game.GameCache;
import com.dissonance.game.w.menu.CoopMenuWorld;

public class MenuQuest extends PauseQuest {
    private World main, coop;
    public static MenuQuest INSTANCE;

    @Override
    public void startQuest() throws Exception {
        Camera.removeBounds();
        Camera.setX(0f);
        Camera.setY(0f);

        INSTANCE = this;
        GameCache.MainMenu = WorldFactory.getWorld("menu.MainMenu");
        main = GameCache.MainMenu;
        setWorld(main);
        main.waitForWorldDisplayed();

        WorldFactory.clearCache();
        GameCache.clear();

        Players.createPlayer1();
        main.waitForWorldDisplayed();
        if (RenderService.getCurrentAlphaValue() == 0f)
            RenderService.INSTANCE.fadeFromBlack(2500);
        Sound.playSound("introtheme");
    }

    public void mainMenu() {
        setWorld(main);
    }

    public void coopMenu() {
        try {
            if (coop == null)
                coop = WorldFactory.getWorld("menu.CoopMenuWorld");
            setWorld(coop);
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        if (RenderService.isInRenderThread()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startGame();
                }
            }).start();
            return;
        }
        if (coop != null) CoopMenuWorld.menu.close();
        Players.createPlayer1(); //Ensure we have a player 1
        GameService.coop_mode = Players.getPlayersWithInput().length > 1;
        RenderService.INSTANCE.fadeToBlack(1000);
        try {
            RenderService.INSTANCE.waitForFade();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setNextQuest(new LoadingQuest());
        try {
            endQuest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "main_menu";
    }
}
