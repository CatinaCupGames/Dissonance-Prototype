package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.w.RoofTopBeginning;
import com.dissonance.game.w.RooftopMid;

public class JumpToBoss extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        RooftopMid.farrand.freeze();
        RooftopMid.jeremiah.freeze();

        RooftopMid.farrand.setUsePhysics(false);
        RooftopMid.jeremiah.setUsePhysics(false);

        Player player1 = Players.getPlayer1();
        if (RooftopMid.farrand.equals(player1.getSprite())) {
            activators.add(RooftopMid.jeremiah);
            RooftopMid.jeremiah.disappear();
            RooftopMid.jeremiah.setUsePhysics(false);
            RooftopMid.jeremiah.setY(RooftopMid.farrand.getY());
            RooftopMid.jeremiah.setX(RooftopMid.farrand.getX() + 32f);
            RooftopMid.jeremiah.appear();
        } else {
            activators.add(RooftopMid.farrand);
            RooftopMid.farrand.disappear();
            RooftopMid.farrand.setUsePhysics(false);
            RooftopMid.farrand.setY(RooftopMid.farrand.getY());
            RooftopMid.farrand.setX(RooftopMid.farrand.getX() + 32f);
            RooftopMid.farrand.appear();
        }

        Dialog.displayDialog("jumpoff");

        RenderService.INSTANCE.fadeToBlack(1500);
        RenderService.INSTANCE.waitForFade();

        GameQuest.INSTANCE.setNextQuest(new BossQuest());
        GameQuest.INSTANCE.endQuest();
    }

    @Override
    protected long triggerTimeout() {
        return 1000;
    }
}
