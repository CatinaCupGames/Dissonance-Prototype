package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.game.quests.Gate_2Quest;


public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);



        GameService.beginQuest(new Gate_2Quest());

        //GameService.beginQuest(new OfficeQuest());
        //GameService.beginQuest(new HallwayQuest());
    }
}
