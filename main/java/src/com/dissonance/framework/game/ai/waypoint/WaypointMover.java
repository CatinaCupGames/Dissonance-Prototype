package com.dissonance.framework.game.ai.waypoint;

import com.dissonance.framework.game.ai.astar.Position;
import javafx.geometry.Pos;

public interface WaypointMover {
    public static WaypointMover Simple = new SimpleWaypointMover();

    /**
     * This method is invoked on the render thread, every frame and should move the {@link com.dissonance.framework.game.ai.waypoint.WaypointSprite} closer to
     * {@link WaypointSprite#getWaypoint()}
     * @param sprite The {@link com.dissonance.framework.game.ai.waypoint.WaypointSprite} to move
     * @return Whether or not the sprite has reached {@link WaypointSprite#getWaypoint()} or not.
     */
    public boolean moveSpriteOneFrame(WaypointSprite sprite, Position sartingPosition);
}
