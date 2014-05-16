package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.AtWaldomar;
import com.dissonance.game.w.WaldoHallway;
import com.dissonance.game.w.WaldomarsMeetingRoom;
import org.lwjgl.util.vector.Vector2f;



/**
 * Created by Henry on 5/15/2014.
 */
public class AtWaldomarScene extends SimpleScene {


    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(3000);
        Vector2f center = Camera.translateToCameraCenter(WaldoHallway.farrand.getVector(), WaldoHallway.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(WaldoHallway.farrand);

        WaldomarsMeetingRoom.farrand.setWaypoint(0f, AtWaldomar.farrand.getY(), WaypointType.SIMPLE);
        WaldomarsMeetingRoom.farrand.setMovementSpeed(3f);

    }
}
