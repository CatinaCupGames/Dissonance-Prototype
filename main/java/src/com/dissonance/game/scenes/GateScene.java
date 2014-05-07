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
        RenderService.INSTANCE.fadeFromBlack(800);
        RenderService.INSTANCE.waitForFade();

        Dialog.displayDialog("GateScene2");
    }
}
