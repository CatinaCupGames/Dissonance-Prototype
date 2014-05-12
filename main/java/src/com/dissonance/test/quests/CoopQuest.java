package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.player.Input;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.w.CityEntrySquare;

public class CoopQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("CityEntrySquare");
        setWorld(world);
        world.waitForWorldDisplayed();

        CityEntrySquare.player1.joinAs(CityEntrySquare.farrand);
    }

    @Override
    public String getName() {
        return "coop";
    }
}
