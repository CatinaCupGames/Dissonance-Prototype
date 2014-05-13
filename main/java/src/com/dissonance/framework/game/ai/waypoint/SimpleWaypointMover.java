package com.dissonance.framework.game.ai.waypoint;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.render.RenderService;

public class SimpleWaypointMover implements WaypointMover {

    public boolean moveSpriteOneFrame(WaypointSprite sprite) {
        Position dest = sprite.getWaypoint();
        if (dest == null)
            return false;

        float xdiff = sprite.getX() - dest.getX();
        float ydiff = sprite.getY() - dest.getY();
        float speed = sprite.getMovementSpeed();
        boolean moved = false;
        if (xdiff <= -4.5f || xdiff >= 4.5f) {
            moved = true;
            sprite.setX(sprite.getX() + (xdiff > 0 ? -speed * RenderService.TIME_DELTA : speed * RenderService.TIME_DELTA));
        }

        if (ydiff <= -4.5 || ydiff >= 4.5f) {
            moved = true;
            sprite.setY(sprite.getY() + (ydiff > 0 ? -speed * RenderService.TIME_DELTA : speed * RenderService.TIME_DELTA));
        }

        return moved;
    }
}
