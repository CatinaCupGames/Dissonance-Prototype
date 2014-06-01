package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.quests.GameQuest;

public class EnterOffice extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        PlayableSprite player = (PlayableSprite)sprite;
        player.setInvincible(true);
        player.setUsePhysics(false);
        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            PlayableSprite p2;
            if (player1.getSprite().equals(player)) {
                activators.add(player2.getSprite());
                p2 = player2.getSprite();

                p2.freeze();
            } else {
                activators.add(player1.getSprite());
                p2 = player1.getSprite();

                p2.freeze();
            }
            p2.setUsePhysics(false);
            p2.setInvincible(true);
            player.freeze();

            p2.setWaypoint(player.getX() + 48, player.getY(), WaypointType.SIMPLE);
            p2.face(Direction.UP);
            player.face(Direction.UP);
            Thread.sleep(500);
            Dialog.displayDialog("jump_into_office_coop");
            Thread.sleep(500);
            player.setMovementSpeed(30f);
            p2.setMovementSpeed(30f);
            player.setAnimation("dodge_up");
            p2.setAnimation("dodge_up");
            player.setWaypoint(player.getX(), 62*16, WaypointType.SIMPLE);
            p2.setWaypoint(p2.getX(), 62f * 16f, WaypointType.SIMPLE);
            player.waitForWaypointReached();
            p2.waitForWaypointReached();
            player.setVisible(false);
            p2.setVisible(false);
            p2.setInvincible(false);
        } else {
            player.freeze();
            player.face(Direction.UP);
            Thread.sleep(500);
            Dialog.displayDialog("jump_into_office_coop"); //TODO Make single player version of dialog
            Thread.sleep(500);
            player.setMovementSpeed(30f);
            player.setAnimation("dodge_up");
            player.setWaypoint(player.getX(), 62*16, WaypointType.SIMPLE);
            player.waitForWaypointReached();
            player.setVisible(false);
        }

        player.setInvincible(false);
        GameQuest.INSTANCE.changeToOffice1();
    }

    @Override
    protected long triggerTimeout() {
        return 1000;
    }
}
