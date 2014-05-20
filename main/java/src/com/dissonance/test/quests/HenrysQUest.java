package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.test.w.officefloor2;

/**
 * Created by Henry on 5/20/2014.
 */
public class HenrysQUest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("officefloor2");
        setWorld(w);
        w.waitForWorldDisplayed();

        Player player1 = Players.createPlayer1();
        player1.joinAs(officefloor2.farrand);
    }

    @Override
    public String getName() {
        return null;
    }
}
