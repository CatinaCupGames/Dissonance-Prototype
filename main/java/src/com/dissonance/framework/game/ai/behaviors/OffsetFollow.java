package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

/**
 * @deprecated The OffsetFollow class used the old steering behavior system.
 * It does absolutely nothing at the moment. It's here just so it doesn't break
 * anything until it's replaced with the updated version.
 */
@Deprecated
public class OffsetFollow implements Behavior {
    public OffsetFollow(AbstractWaypointSprite sprite, AbstractWaypointSprite target, Position offset) {
    }

    @Override
    public void update() {

    }
}
