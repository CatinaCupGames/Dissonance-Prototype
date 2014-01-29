package com.dissonance.framework.game.ai.waypoint;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.render.RenderService;

public class WaypointMover {
    public static final float SPEED = 10; //Maybe make this sprite specific?

    public static boolean moveSpriteOneFrame(WaypointSprite sprite) {
        Position dest = sprite.getWaypoint();
        if (dest == null)
            return false;

        float xdiff = sprite.getX() - dest.getX();
        float ydiff = sprite.getY() - dest.getY();
        boolean moved = false;
        if (xdiff <= -0.5f || xdiff >= 0.5f) {
            moved = true;
            sprite.setX(sprite.getX() + (xdiff > 0 ? -SPEED * RenderService.TIME_DELTA : SPEED * RenderService.TIME_DELTA));
        }

        if (ydiff <= -0.5 || ydiff >= 0.5f) {
            moved = true;
            sprite.setY(sprite.getY() + (ydiff > 0 ? -SPEED * RenderService.TIME_DELTA : SPEED * RenderService.TIME_DELTA));
        }

        return moved;
    }
}
