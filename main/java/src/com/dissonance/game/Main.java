package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.game.quests.DisclaimerQuest;
import com.dissonance.game.quests.HallwayQuest;
import com.dissonance.game.quests.OfficeQuest;

public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);
        //GameService.beginQuest(new DisclaimerQuest());
        GameService.beginQuest(new OfficeQuest());
        //GameService.beginQuest(new HallwayQuest());
    }
}
