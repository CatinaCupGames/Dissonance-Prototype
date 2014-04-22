package com.dissonance.framework.game.ai.waypoint;

public interface WaypointMover {
    public static WaypointMover Simple = new SimpleWaypointMover();

    /**
     * This method is invoked on the render thread, every frame and should move the {@link com.dissonance.framework.game.ai.waypoint.WaypointSprite} closer to
     * {@link WaypointSprite#getWaypoint()}
     * @param sprite The {@link com.dissonance.framework.game.ai.waypoint.WaypointSprite} to move
     * @return Whether or not the sprite has reached {@link WaypointSprite#getWaypoint()} or not.
     */
    public boolean moveSpriteOneFrame(WaypointSprite sprite);

    /**
     * Get how fast this WaypointMover moves the sprite. This is not used by the framework, but some implementations uses this.
     * @return How fast this WaypointMover moves a sprite.
     */
    public float getSpeed();
}
