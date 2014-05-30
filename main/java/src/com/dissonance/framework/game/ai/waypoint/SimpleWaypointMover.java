package com.dissonance.framework.game.ai.waypoint;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.render.RenderService;

public class SimpleWaypointMover implements WaypointMover {

    public boolean moveSpriteOneFrame(WaypointSprite sprite, Position startPosition) {
        Position dest = sprite.getWaypoint();
        if (dest == null)
            return false;

        float xdiff = sprite.getX() - dest.getX();
        float ydiff = sprite.getY() - dest.getY();
        float speed = sprite.getMovementSpeed();
        boolean moved = false;
        if (xdiff >= 4.5f && startPosition.getX() > dest.getX()) {
            moved = true;
            sprite.setX(sprite.getX() + (-speed * RenderService.TIME_DELTA));
        } else if (xdiff <= -4.5 && startPosition.getX() < dest.getX()) {
            moved = true;
            sprite.setX(sprite.getX() + (speed * RenderService.TIME_DELTA));
        }

        if (ydiff >= 4.5f && startPosition.getY() > dest.getY()) {
            moved = true;
            sprite.setY(sprite.getY() + (-speed * RenderService.TIME_DELTA));
        } else if (ydiff <= -4.5f && startPosition.getY() < dest.getY()) {
            moved = true;
            sprite.setY(sprite.getY() + (speed * RenderService.TIME_DELTA));
        }

        return moved;
    }
}
