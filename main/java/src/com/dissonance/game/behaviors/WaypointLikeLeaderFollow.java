package com.dissonance.game.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

import java.util.List;
/**
 * Waypoint like Leader follow is a behavior that works similarly to the {@link com.dissonance.framework.game.ai.behaviors.LeaderFollow}
 * behavior. It will make the target sprite follow a leader and make sure there's
 * a certain offset between them. They move in a waypoint like fashion.
 */
public class WaypointLikeLeaderFollow implements Behavior {
    static final float RECALCULATE_DISTANCE = 15f;
    private static final float SQRT2 = (float) Math.sqrt(2);

    private AbstractWaypointSprite sprite;
    private AbstractWaypointSprite leader;
    private Vector offset;

    private Vector cOffset = new Vector();
    private Vector oldPos;
    private List<Position> nodes;

    public WaypointLikeLeaderFollow(AbstractWaypointSprite sprite, AbstractWaypointSprite leader, Vector offset) {
        this.sprite = sprite;
        this.leader = leader;
        this.offset = offset;
        oldPos = leader.getPositionVector();

        calculateCOffset();

        NodeMap map = sprite.getWorld().getNodeMap();
        int tw = sprite.getWorld().getTiledData().getTileWidth();
        int th = sprite.getWorld().getTiledData().getTileHeight();
        Position ps = new Position(leader.getPositionVector().add(cOffset));
        nodes = map.findPath(sprite.getPosition().shrink(tw, th), ps.shrink(tw, th));
        nodes.remove(0);
    }

    @Override
    public void update() {
        if (shouldRecalculate()) {
            NodeMap map = sprite.getWorld().getNodeMap();
            int tw = sprite.getWorld().getTiledData().getTileWidth();
            int th = sprite.getWorld().getTiledData().getTileHeight();

            calculateCOffset();

            Position ps = new Position(leader.getPositionVector().add(cOffset));
            nodes = map.findPath(sprite.getPosition().shrink(tw, th), ps.shrink(tw, th));
            nodes.remove(0);
        }

        Vector target = null;

        if (nodes != null && nodes.size() >= 1) {
            target = nodes.get(0).vector().multiply(16f);

            if (sprite.getPositionVector().subtract(target).length() <= 5) {
                if (nodes.size() >= 1) {
                    nodes.remove(0);
                }
            }
        }

        if (target == null) {
            return;
        }

        float xdiff = sprite.getX() - target.x;
        float ydiff = sprite.getY() - target.y;
        float speed = sprite.getMovementSpeed();
        boolean moved = false;

        float difx = 4.5f;
        float dify = 4.5f;

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
            sprite.face(leader.getFacingDirection());
        }
    }

    private boolean shouldRecalculate() {
        Vector pos = leader.getPositionVector();
        Vector newPos;
        newPos = pos.subtract(oldPos);
        boolean rc = (Math.abs(newPos.x) >= RECALCULATE_DISTANCE || Math.abs(newPos.y) >= RECALCULATE_DISTANCE);

        if (rc) {
            oldPos = pos;
        }

        return rc;
    }

    private void calculateCOffset() {
        switch (leader.getFacingDirection()) {
            case UP:
                cOffset.x = offset.x;
                cOffset.y = offset.y;
                break;
            case DOWN_LEFT:
                cOffset.x = (offset.x + offset.y) / SQRT2;
                cOffset.y = (offset.x - offset.y) / SQRT2;
                break;
            case UP_RIGHT:
                cOffset.x = (offset.x - offset.y) / SQRT2;
                cOffset.y = (offset.x + offset.y) / SQRT2;
                break;
            case UP_LEFT:
                cOffset.x = (offset.y + offset.x) / SQRT2;
                cOffset.y = (offset.y - offset.x) / SQRT2;
                break;
            case DOWN_RIGHT:
                cOffset.x = -(offset.y - offset.x) / SQRT2;
                cOffset.y = -(offset.y + offset.x) / SQRT2;
                break;
            case DOWN:
                cOffset.y = -offset.y;
                break;
            case RIGHT:
                cOffset.x = -offset.y;
                cOffset.y = offset.x + 0;
                break;
            case LEFT:
                cOffset.x = offset.y + 0;
                cOffset.y = offset.x + 0;
                break;
        }
    }

    public List<Position> getNodes() {
        return nodes;
    }
}
