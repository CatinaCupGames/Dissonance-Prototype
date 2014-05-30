package com.dissonance.game.quests;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.w.CityEntrySquare2;

public class BossQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        WorldFactory.clearCache();
        World world = WorldFactory.getWorld("CityEntrySquare", new CityEntrySquare2());
        setWorld(world);
        world.waitForWorldDisplayed();


    }

    @Override
    public String getName() {
        return "The Boss";
    }
}
