package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.w.WaldomarsMeetingRoom;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Jmerrill on 5/6/2014.
 */
public class OfficeScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(1500);
        Vector2f center = Camera.translateToCameraCenter(WaldomarsMeetingRoom.farrand.getVector(), WaldomarsMeetingRoom.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(WaldomarsMeetingRoom.farrand);
        Position farrandTarget = WaldomarsMeetingRoom.farrand.getPosition();
        Position jeremiahTarget = WaldomarsMeetingRoom.jeremiah.getPosition();

        WaldomarsMeetingRoom.guard1.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard1.setWaypoint(4f * 16, 11f * 16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.guard2.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard2.setWaypoint(9f * 16, 11f * 16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.farrand.setMovementSpeed(4f);
        WaldomarsMeetingRoom.farrand.setWaypoint(102.95f, 8f*16, WaypointType.SIMPLE);



        WaldomarsMeetingRoom.guard3.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard3.setWaypoint(14f * 16, 13f * 16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.guard4.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard4.setWaypoint(19f * 16, 13f * 16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(4f);
        WaldomarsMeetingRoom.jeremiah.setWaypoint(16f*16, 12f*16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.guard5.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard5.setWaypoint(26f * 16, 9f * 16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.farrand.waitForWaypointReached();
        WaldomarsMeetingRoom.farrand.setAnimation("walk_front");
        WaldomarsMeetingRoom.farrand.pauseAnimation();
        WaldomarsMeetingRoom.farrand.setFrame(1);

        WaldomarsMeetingRoom.guard1.waitForWaypointReached();

        Dialog.displayDialog("Waldos meeting");

        Camera.shake(Direction.DOWN, 4000L, 5, 0.5);
        Sound.playSound("firespell").setPitch(0.25f);

        WaldomarsMeetingRoom.guard1.setMovementSpeed(20f);
        WaldomarsMeetingRoom.guard1.setWaypoint(0, 11f * 16, WaypointType.SIMPLE);





    }
}