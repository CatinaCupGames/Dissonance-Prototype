package com.dissonance.framework.game.ai.waypoint;

public enum WaypointType {
    /**
     * Represents a simple waypoint addition to the waypoint queue. No additional waypoints are added.
     */
    SMART,
    /**
     * Represents a smart waypoint addition to the waypoint queue. The framework will use A* to find the best path
     * to the waypoint added and add additional waypoints to the waypoint queue to reach the waypoint added.
     */
    SIMPLE
}
