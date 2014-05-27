package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.Direction;

public class EnterOffice extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        PlayableSprite player = (PlayableSprite)sprite;
        player.freeze();
        player.face(Direction.UP);
        Thread.sleep(500);
        Dialog.displayDialog("jump_into_office");
        Thread.sleep(500);
        player.setMovementSpeed(30f);
        player.setAnimation("dodge_up");
        player.setWaypoint(player.getX(), 62*16, WaypointType.SIMPLE);
        player.waitForWaypointReached();
        player.setVisible(false);


    }

    @Override
    protected long triggerTimeout() {
        return 1000;
    }
}
