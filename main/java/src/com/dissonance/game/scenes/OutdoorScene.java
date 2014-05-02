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
        Vector2f center = Camera.translateToCameraCenter(ToZesilia.var1.getVector(), ToZesilia.var1.getHeight());
        center.x -= 330;
        Camera.setPos(center);
        center.x -= 400;
        Camera.linearMovement(center, 7500);
        Position farrandTarget = ToZesilia.var1.getPosition();
        Position jerymanglerTarget = ToZesilia.var2.getPosition();

        ToZesilia.var1.setMovementSpeed(8f);
        ToZesilia.var1.setWaypoint(new Position(farrandTarget.getX() - 600, farrandTarget.getY()), WaypointType.SIMPLE);
        ToZesilia.var2.setMovementSpeed(8f);
        ToZesilia.var2.setWaypoint(new Position(jerymanglerTarget.getX() - 700, jerymanglerTarget.getY()), WaypointType.SIMPLE);
        Camera.waitForEndOfMovement();
    }
}
