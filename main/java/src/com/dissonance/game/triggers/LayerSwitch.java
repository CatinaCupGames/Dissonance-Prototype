package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;

import java.util.ArrayList;

public class LayerSwitch extends AbstractTrigger {
    static ArrayList<Sprite> climbers = new ArrayList<Sprite>();

    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        sprite.setMovementSpeed(10);
        if (sprite.getLayer() == 2) {
            climbers.add(sprite);

            sprite.setUsePhysics(false);
            sprite.rawSetX(getParent().getX() + (getParent().getWidth() / 2f));
            sprite.rawSetY(getParent().getY() + (getParent().getHeight() / 2f));
            sprite.setLayer(6);
            if (sprite instanceof PlayableSprite)
                ((PlayableSprite)sprite).freeze(true, LayerSwitch.class);
            sprite.setWaypoint(sprite.getX(), sprite.getY() - (4 * 16f), WaypointType.SIMPLE);
            sprite.waitForWaypointReached();
            if (sprite instanceof PlayableSprite)
                ((PlayableSprite)sprite).unfreeze(LayerSwitch.class);
            sprite.setUsePhysics(true);

            climbers.remove(sprite);
        } else if (sprite.getLayer() == 6) {
            climbers.add(sprite);

            sprite.setUsePhysics(false);
            sprite.rawSetX(getParent().getX() + (getParent().getWidth() / 2f));
            sprite.rawSetY(getParent().getY() + (getParent().getHeight() / 2f));
            if (sprite instanceof PlayableSprite)
                ((PlayableSprite)sprite).freeze(true, LayerSwitch.class);
            sprite.setWaypoint(sprite.getX(), sprite.getY() + (2 * 16f), WaypointType.SIMPLE);
            sprite.waitForWaypointReached();
            if (sprite instanceof PlayableSprite)
                ((PlayableSprite)sprite).unfreeze(LayerSwitch.class);
            sprite.setLayer(2);
            sprite.setUsePhysics(true);

            climbers.remove(sprite);
        }
    }

    @Override
    protected long triggerTimeout() {
        return 500;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

}
