package com.dissonance.framework.game.ai.waypoint;

import com.dissonance.framework.game.ai.astar.Position;

public interface WaypointSprite {

    public float getX();

    public float getY();

    public void setX(float x);

    public void setY(float y);

    public Position getWaypoint();
}
