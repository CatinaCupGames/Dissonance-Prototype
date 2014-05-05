package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.ToZesilia;
import org.lwjgl.util.vector.Vector2f;

public class OutdoorScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(10000f);
        Vector2f center = Camera.translateToCameraCenter(ToZesilia.farrand.getVector(), ToZesilia.farrand.getHeight());
        //center.x -= 330;
        Camera.setPos(center);
        center.x -= 400;
        Camera.linearMovement(center, 7500);
        Position farrandTarget = ToZesilia.farrand.getPosition();
        Position jeremiahTarget = ToZesilia.jeremiah.getPosition();

        ToZesilia.farrand.setMovementSpeed(8f);
        ToZesilia.farrand.setWaypoint(new Position(farrandTarget.getX() - 600, farrandTarget.getY()), WaypointType.SIMPLE);
        ToZesilia.jeremiah.setMovementSpeed(8f);
        ToZesilia.jeremiah.setWaypoint(new Position(jeremiahTarget.getX() - 700, jeremiahTarget.getY()), WaypointType.SIMPLE);
        Camera.waitForEndOfMovement();
    }
}
