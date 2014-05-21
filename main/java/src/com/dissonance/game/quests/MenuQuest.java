package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import org.lwjgl.util.vector.Vector2f;

public class MenuQuest extends PauseQuest {
    private World main, coop;
    public static MenuQuest INSTANCE;

    @Override
    public void startQuest() throws Exception {
        INSTANCE = this;
        main = WorldFactory.getWorld("menu.MainMenu");
        coop = WorldFactory.getWorld("menu.CoopMenuWorld");
        setWorld(main);
        main.waitForWorldDisplayed();
        RenderService.INSTANCE.fadeToBlack(1f);
        RenderService.INSTANCE.fadeFromBlack(2500);
        //TODO Display menu and wait for option to be chosen

        //TODO Remove, temp code
        //setNextQuest(new DisclaimerQuest()); //Set the next quest
        //endQuest(); //End this quest
    }

    public void mainMenu() {
        setWorld(main);
    }

    public void coopMenu() {
        setWorld(coop);
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
        Players.createPlayer1(); //Ensure we have a player 1
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
