package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.GameCache;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.w.OutsideFighting;
import com.dissonance.game.w.RoofTopBeginning;
import org.lwjgl.util.vector.Vector2f;

public class Garbage extends AbstractTrigger {
    @Override
    protected void onTrigger(PlayableSprite sprite) throws Throwable {
        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        if (player2 == null) {
            animateSprite(sprite);
        } else {
            sprite.freeze();
            PlayableSprite toMove = null;
            if (player1.getSprite() == sprite)
                toMove = player2.getSprite();
            else
                toMove = player1.getSprite();

            //TODO Display dialog like "Over here!"

            toMove.freeze();
            toMove.setMovementSpeed(20);
            toMove.setWaypoint(sprite.getX() - 20f, sprite.getY(), WaypointType.SMART);
            toMove.waitForEndOfWaypointQueue();
            toMove.face(Direction.RIGHT);

            animateSprite(sprite);
            animateSprite(toMove);
        }

        finish();
    }

    private void animateSprite(PlayableSprite sprite) throws InterruptedException {
        sprite.freeze();
        sprite.ignoreCollisionWith(RoofTopBeginning.exit);
        sprite.setMovementSpeed(3);
        sprite.setWaypoint(sprite.getX() - 48f, sprite.getY(), WaypointType.SIMPLE);
        sprite.waitForWaypointReached();
        Thread.sleep(300);
        sprite.setMovementSpeed(30f);
        sprite.setWaypoint(sprite.getX() + RoofTopBeginning.exit.getX(), sprite.getY(), WaypointType.SIMPLE);
        Thread.sleep(300L);
        sprite.setVisible(false);
        sprite.clearWaypoints();
        Camera.stopFollowing(sprite);
    }

    private void finish() throws InterruptedException {
        Camera.linearMovement(new Vector2f(Camera.getX(), Camera.getY() + 200), 3000);
        Thread.sleep(1500);
        Camera.clearMovementRequests();
        GameQuest.INSTANCE.changeToOutside1();
        OutsideFighting.farrand.unfreeze();
        OutsideFighting.jeremiah.unfreeze();
    }

    @Override
    protected long triggerTimeout() {
        return 10000;
    }
}
