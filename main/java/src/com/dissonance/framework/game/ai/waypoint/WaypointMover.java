package com.dissonance.framework.game.ai.waypoint;

public interface WaypointMover {
    public static WaypointMover Simple = new SimpleWaypointMover();


    public boolean moveSpriteOneFrame(WaypointSprite sprite);

    public float getSpeed();
}
