package com.dissonance.game.scenes;


import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.WaldoHallway;
import org.lwjgl.util.vector.Vector2f;

<<<<<<< HEAD

=======
>>>>>>> 572df41aea82596c4b4e86e8bca20dab885e2245
public class HallwayScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(3000);
        Vector2f center = Camera.translateToCameraCenter(WaldoHallway.farrand.getVector(), WaldoHallway.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(WaldoHallway.farrand);
        //guard 1, then guard 2, THEN farrand, then guards 3 and 4, then jeremiah, then guard 5

        WaldoHallway.guard1.setMovementSpeed(6f);
        WaldoHallway.guard1.setWaypoint(2,5*16,WaypointType.SIMPLE);

        WaldoHallway.guard2.setMovementSpeed(6f);
        WaldoHallway.guard2.setWaypoint(0,9*16,WaypointType.SIMPLE);

        WaldoHallway.farrand.setMovementSpeed(6f);
        WaldoHallway.farrand.setWaypoint(0,7*16,WaypointType.SIMPLE);

        WaldoHallway.guard3.setMovementSpeed(6f);
        WaldoHallway.guard3.setWaypoint(0,5*16,WaypointType.SIMPLE);

        WaldoHallway.guard4.setMovementSpeed(6f);
        WaldoHallway.guard4.setWaypoint(0,9*16,WaypointType.SIMPLE);

        WaldoHallway.jeremiah.setMovementSpeed(6f);
        WaldoHallway.jeremiah.setWaypoint(0,7*16,WaypointType.SIMPLE);

        WaldoHallway.guard5.setMovementSpeed(6f);
        WaldoHallway.guard5.setWaypoint(0,7*16,WaypointType.SIMPLE);

        Thread.sleep(4350);
        RenderService.INSTANCE.fadeToBlack(1500);
        RenderService.INSTANCE.waitForFade();



    }
}
