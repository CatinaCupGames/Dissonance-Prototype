package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.sprites.factory.ElevatorDoor;
import com.dissonance.game.w.FactoryFloorCat;

public class ElevatorScene extends SimpleScene {
    private ElevatorDoor door;
    public ElevatorScene(ElevatorDoor door) {
        this.door = door;
    }
    @Override
    protected void playScene() throws Throwable {
        PlayableSprite sprite = Players.getPlayer1().getSprite();

        Player player2 = Players.getPlayer(2);
        PlayableSprite p2 = null;
        if (player2 != null && player2.getSprite() != null) {
            p2 = player2.getSprite();
            p2.ignoreCollisionWith(door);
            p2.setWaypoint(new Position(sprite.getX(), sprite.getY() + 32f), WaypointType.SMART);
            p2.waitForEndOfWaypointQueue();
        }

        sprite.setLayer(1);
        sprite.setMovementSpeed(8f);
        sprite.setWaypoint(29f * 16f, 7f * 16f, WaypointType.SIMPLE);
        if (p2 != null) {
            p2.setLayer(2);
            p2.setMovementSpeed(8f);
            p2.setWaypoint(28f * 16f, 7 * 16f, WaypointType.SIMPLE);
        }
        sprite.waitForWaypointReached();
        if (p2 != null)
            p2.waitForWaypointReached();

        sprite.setX(29f * 16f);
        sprite.setY(7f * 16f);

        if (p2 != null) {
            p2.setX(28f * 16f);
            p2.setY(7f * 16f);
        }

        Thread.sleep(1200);

        sprite.face(Direction.DOWN);
        if (p2 != null)
            p2.face(Direction.DOWN);
        door.reverseAnimation(true);
        door.playAnimation();
        door.waitForAnimationEnd();
        RenderService.INSTANCE.fadeToBlack(1300);
        RenderService.INSTANCE.waitForFade();
    }
}
