package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.Direction;

public class EnterOffice extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        PlayableSprite player = (PlayableSprite)sprite;
        player.setUsePhysics(false);
        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            activators.add(player2.getSprite());
            PlayableSprite p2 = player2.getSprite();
            p2.freeze();

            player.freeze();

            p2.setWaypoint(player.getX() + 48, player.getY(), WaypointType.SIMPLE);
        }
        player.face(Direction.UP);
        Thread.sleep(500);
        Dialog.displayDialog("jump_into_office");
        Thread.sleep(500);
        player.setMovementSpeed(30f);
        player.setAnimation("dodge_up");
        player.setWaypoint(player.getX(), 62*16, WaypointType.SIMPLE);
        player.waitForWaypointReached();
        player.setVisible(false);


    }

    @Override
    protected long triggerTimeout() {
        return 1000;
    }
}
