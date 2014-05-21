package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;

public class LayerSwitch extends AbstractTrigger {
    @Override
    protected void onTrigger(PlayableSprite sprite) throws Throwable {
        if (sprite.getLayer() == 2) {
            sprite.rawSetX(getParent().getX());
            sprite.rawSetY(getParent().getY());
            sprite.setLayer(6);
            sprite.freeze();
            sprite.setWaypoint(sprite.getX(), sprite.getY() - (4 * 16f), WaypointType.SIMPLE);
            sprite.waitForWaypointReached();
            sprite.unfreeze();
        } else if (sprite.getLayer() == 6) {
            sprite.rawSetX(getParent().getX());
            sprite.rawSetY(getParent().getY());
            sprite.freeze();
            sprite.setWaypoint(sprite.getX(), sprite.getY() + (2 * 16f), WaypointType.SIMPLE);
            sprite.waitForWaypointReached();
            sprite.unfreeze();
            sprite.setLayer(2);
        }
    }

    @Override
    protected long triggerTimeout() {
        return 500;
    }
}
