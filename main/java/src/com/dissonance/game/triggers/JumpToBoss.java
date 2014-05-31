package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.RooftopMid;

public class JumpToBoss extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        RooftopMid.farrand.freeze();
        RooftopMid.jeremiah.freeze();

        Dialog.displayDialog("jumpoff");

        Thread.sleep(1000);

        RooftopMid.farrand.setMovementSpeed(20f);
        RooftopMid.farrand.setWaypoint(RooftopMid.farrand.getX(), 104 * 16, WaypointType.SIMPLE);

        RooftopMid.jeremiah.setMovementSpeed(20f);
        RooftopMid.jeremiah.setWaypoint(RooftopMid.jeremiah.getX(), 104 * 16, WaypointType.SIMPLE);

        RenderService.INSTANCE.fadeToBlack(1500);


    }

    @Override
    protected long triggerTimeout() {
        return 1000;
    }
}
