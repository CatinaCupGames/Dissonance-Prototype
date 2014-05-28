package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.quests.CatQuest;
import com.dissonance.game.quests.LoadingQuest;
import com.dissonance.game.quests.MenuQuest;


public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);
        RenderService.ZOOM_SCALE = 2;
        GameService.beginQuest(new CatQuest());


    }
}
