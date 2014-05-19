package com.dissonance.game.quests;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.w.RoofTopBeginning;

public class GameQuest  extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        WorldFactory.clearCache();
        World w = WorldFactory.getWorld("RoofTopBeginning");
        World level2 = WorldFactory.getWorld("OutsideFighting");
        setWorld(w);
        w.waitForWorldDisplayed();

        Player player1 = Players.createPlayer1();
        player1.joinAs(RoofTopBeginning.farrand);
        RoofTopBeginning.farrand.setCurrentWeapon(Weapon.getWeapon("farrandstaff").createItem(RoofTopBeginning.farrand));
    }

    @Override
    public String getName() {
        return "the actual game";
    }
}
