package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.EntryPart1;

/**
 * Created by Henry on 5/2/2014.
 */
public class GateScene extends SimpleScene{

    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(1500);
        RenderService.INSTANCE.waitForFade();

        Camera.followSprite(EntryPart1.farrand);

      EntryPart1.farrand.setWaypoint(
              new Position(EntryPart1.farrand.getX() - 752,EntryPart1.farrand.getY()),
              WaypointType.SIMPLE);

        EntryPart1.jeremiah.setWaypoint(
                new Position(EntryPart1.jeremiah.getX() - 752, EntryPart1.jeremiah.getY()),
                WaypointType.SIMPLE);

        Dialog.displayDialog("entering_zesilia1");

        EntryPart1.farrand.waitForWaypointReached();



        Camera.followSprite(EntryPart1.guard);

        Dialog.displayDialog("entering_zesilia2")        ;

        //open he gates







    }
}
