package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;

/**
 * Created by Henry on 5/20/2014.
 */
public class HenrysQUest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("officefloor2");
        setWorld(w);
    }

    @Override
    public String getName() {
        return null;
    }
}
