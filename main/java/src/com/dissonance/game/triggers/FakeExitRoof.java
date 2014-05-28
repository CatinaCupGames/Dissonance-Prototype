package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.w.RoofTopBeginning;

public class FakeExitRoof extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite s) throws Throwable {
        PlayableSprite sprite = (PlayableSprite)s;
        sprite.setUsePhysics(false);
        sprite.setIsInvincible(true);
        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        sprite.freeze();
        if (player2 == null || player2.getSprite() == null) {
            PlayableSprite toTalk;
            String did;
            if (sprite == RoofTopBeginning.farrand) {
                toTalk = RoofTopBeginning.jeremiah;
                did = "FakeExitFarrand";
            }
            else {
                toTalk = RoofTopBeginning.farrand;
                did = "FakeExitJeremiah";
            }
            toTalk.rawSetX(sprite.getX());
            toTalk.rawSetY(sprite.getY() + (32f / 2f) + 8f);
            toTalk.face(Direction.UP);
            toTalk.appear();
            Dialog.displayDialog(did);
            toTalk.disappear();
            sprite.setWaypoint(sprite.getX() + (16f * 3f), sprite.getY(), WaypointType.SIMPLE);
            sprite.waitForWaypointReached();
            sprite.setInvincible(false);
            sprite.setUsePhysics(true);
            sprite.unfreeze();
        } else {
            player1.getSprite().setIsInvincible(true);
            player2.getSprite().setIsInvincible(true);
            if (sprite == player1.getSprite()) { //Farrand
                Direction direction = sprite.directionTowards(player2.getSprite());
                sprite.face(direction);
                player2.getSprite().face(direction.opposite());
                Dialog.displayDialog("FakeExitFarrand");
            } else {
                Direction direction = sprite.directionTowards(player1.getSprite());
                sprite.face(direction);
                player1.getSprite().face(direction.opposite());
                Dialog.displayDialog("FakeExitJeremiah");
            }

            player1.getSprite().setIsInvincible(false);
            player2.getSprite().setIsInvincible(false);
            player1.getSprite().unfreeze();
            player2.getSprite().unfreeze();
        }
    }

    @Override
    protected long triggerTimeout() {
        return 10000;
    }
}
