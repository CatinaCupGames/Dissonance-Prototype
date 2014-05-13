package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.game.quests.DisclaimerQuest;
<<<<<<< HEAD
import com.dissonance.game.quests.MenuQuest;
import com.dissonance.game.quests.OfficeQuest;
=======
import com.dissonance.game.quests.HallwayQuest;
>>>>>>> master

public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);
<<<<<<< HEAD
        GameService.beginQuest(new MenuQuest());
=======
        GameService.beginQuest(new DisclaimerQuest());
        //GameService.beginQuest(new OfficeQuest());
        //GameService.beginQuest(new HallwayQuest());
>>>>>>> master
    }
}
