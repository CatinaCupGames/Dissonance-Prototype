package com.dissonance.test;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.World;
import com.dissonance.test.quests.AITestQuest;
import com.dissonance.test.quests.ConveyorTest;
import com.dissonance.test.quests.TestQuest;

public class Main {

    public static void main(String[] args) throws Exception {
        World.setDefaultLoaderPackage("com.dissonance.test.w");

        GameService.loadEssentials(args);
        GameService.beginQuest(new ConveyorTest());
    }
}
