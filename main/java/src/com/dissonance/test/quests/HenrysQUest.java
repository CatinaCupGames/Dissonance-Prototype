package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.w.RoofTopBeginning;
import com.dissonance.game.w.officefloor2;
import com.dissonance.test.w.RooftopMid;

/**
 * Created by Henry on 5/20/2014.
 */
public class HenrysQUest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World.setDefaultLoaderPackage("com.dissonance.game.w");

        World w = WorldFactory.getWorld("RooftopMid");
        setWorld(w);
        w.waitForWorldDisplayed();

        Player player1 = Players.createPlayer1();
        player1.joinAs(RooftopMid.farrand);
    }

    @Override
    public String getName() {
        return null;
    }
}
