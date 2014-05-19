package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.game.quests.DisclaimerQuest;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.quests.GateQuest;
import com.dissonance.game.quests.LoadingQuest;


public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);



        GameService.beginQuest(new LoadingQuest());



    }
}
