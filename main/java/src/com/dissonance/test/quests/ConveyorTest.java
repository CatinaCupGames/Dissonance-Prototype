package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;

public class ConveyorTest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("test_stuff");
        setWorld(world);
        world.waitForWorldLoaded();
    }

    @Override
    public String getName() {
        return null;
    }
}
