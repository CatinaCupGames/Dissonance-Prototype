package com.dissonance.game.quests;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.GameCache;
import com.dissonance.game.w.RoofTopBeginning;

public class GameQuest  extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        setWorld(GameCache.RoofTopBeginning);
        GameCache.RoofTopBeginning.waitForWorldDisplayed();

        Camera.stopFollowing();

        Player player1 = Players.createPlayer1();
        if (player1.isPlaying())
            player1.changeSprite(RoofTopBeginning.farrand);
        else
            player1.joinAs(RoofTopBeginning.farrand);

        Player player2 = Players.getPlayer(2);
        if (player2 != null) {
            if (player2.isPlaying())
                player2.changeSprite(RoofTopBeginning.jeremiah);
            else
                player2.join();
        }


        RoofTopBeginning.farrand.setCurrentWeapon(Weapon.getWeapon("farrandstaff").createItem(RoofTopBeginning.farrand));
    }

    @Override
    public String getName() {
        return "the actual game";
    }
}
