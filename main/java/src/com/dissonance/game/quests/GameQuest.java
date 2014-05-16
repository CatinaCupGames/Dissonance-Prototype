package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;

public class GameQuest  extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        WorldFactory.clearCache();
        World w = WorldFactory.getWorld("RoofTopBeginning");
        World level2 = WorldFactory.getWorld("OutsideFighting");
        setWorld(w);
        w.waitForWorldDisplayed();
    }

    @Override
    public String getName() {
        return "the actual game";
    }
}
