package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

/**
 * Seek is a steering behavior that will make the sprite move towards the target.
 * The implementation of this behavior is sillier to {@link com.dissonance.framework.game.ai.behaviors.Seek}, with the difference
 * that it uses a {@link com.dissonance.framework.game.ai.waypoint.WaypointType#SIMPLE} like movement behavior.
 *
 * @see com.dissonance.framework.game.ai.waypoint.WaypointType#SIMPLE
 * @see com.dissonance.framework.game.ai.behaviors.Seek
 */
public class WaypointLikeSeek implements Behavior {
    private final AbstractWaypointSprite sprite;
    private Vector target;

    public WaypointLikeSeek(AbstractWaypointSprite sprite, Position target) {
        this.sprite = sprite;
        this.target = new Vector(target.getX(), target.getY());
    }

    @Override
    public void update() {
        float xdiff = sprite.getX() - target.x;
        float ydiff = sprite.getY() - target.y;
        float speed = sprite.getMovementSpeed();
        boolean moved = false;

        float difx = sprite.getWidth() / 2f;
        float dify = sprite.getHeight() / 2f;

        if (xdiff <= -difx || xdiff >= difx) {
            moved = true;
            sprite.setX(sprite.getX() + (xdiff > 0 ? -speed * RenderService.TIME_DELTA : speed * RenderService.TIME_DELTA));
            sprite.setFacingDirection(xdiff > 0 ? Direction.LEFT : Direction.RIGHT);
        }

        if (ydiff <= -dify || ydiff >= dify) {
            sprite.setY(sprite.getY() + (ydiff > 0 ? -speed * RenderService.TIME_DELTA : speed * RenderService.TIME_DELTA));
            if (moved) {
                sprite.setFacingDirection(ydiff > 0 ? sprite.getFacingDirection().add(Direction.UP) : sprite.getFacingDirection().add(Direction.DOWN));
            } else {
                sprite.setFacingDirection(ydiff > 0 ? Direction.UP : Direction.DOWN);
            }
            moved = true;
        }

        if (!moved) {
            sprite.setBehavior(null);
        }
    }

    public void setTarget(Vector target) {
        this.target = target;
    }

    public void setTarget(Position position) {
        this.target = position.vector();
    }
}
