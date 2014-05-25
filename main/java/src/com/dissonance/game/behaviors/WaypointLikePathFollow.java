package com.dissonance.game.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.FiniteBehavior;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

import java.util.List;

public class WaypointLikePathFollow implements FiniteBehavior {

    private AbstractWaypointSprite sprite;
    private List<Position> nodes;

    private FiniteBehaviorEvent.OnFinished onFinishedListener;

    public WaypointLikePathFollow(AbstractWaypointSprite sprite, Position target) {
        this.sprite = sprite;
        NodeMap map = sprite.getWorld().getNodeMap();
        int tw = sprite.getWorld().getTiledData().getTileWidth();
        int th = sprite.getWorld().getTiledData().getTileHeight();
        nodes = map.findPath(new Position(sprite.getX(), sprite.getY()).shrink(tw, th), target.shrink(tw, th));
    }

    @Override
    public void update() {
        Vector target = null;

        if (nodes != null && nodes.size() >= 1) {
            target = nodes.get(0).vector().multiply(16f);

            if (sprite.getFeetVector().subtract(target).length() <= 20) {
                if (nodes.size() >= 1) {
                    nodes.remove(0);
                }
            }
        }

        if (target == null) {
            if (onFinishedListener != null) {
                onFinishedListener.onFinished(this);
            }
            return;
        }

        float xdiff = sprite.getX() - target.x;
        float ydiff = sprite.getY() - target.y;
        float speed = sprite.getMovementSpeed();
        boolean moved = false;

        float difx = sprite.getWidth() / 4f;
        float dify = sprite.getHeight() / 4f;

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

        if (nodes.size() == 0 && !moved) {
            if (onFinishedListener != null) {
                onFinishedListener.onFinished(this);
            }
        }
    }

    public List<Position> getNodes() {
        return nodes;
    }

    public FiniteBehaviorEvent.OnFinished getOnFinishedListener() {
        return onFinishedListener;
    }

    public void setOnFinishedListener(FiniteBehaviorEvent.OnFinished onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }
}
