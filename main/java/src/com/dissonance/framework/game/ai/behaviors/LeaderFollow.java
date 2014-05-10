package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

import java.util.List;

/**
 * Leader follow is a behavior that works similarly to the {@link PathFollow}
 * behavior. It will make the target sprite follow a leader and make sure there's
 * a certain offset between them.
 */
public final class LeaderFollow implements Behavior {

    static final float RECALCULATE_DISTANCE = 15f;
    private static final float SQRT2 = (float) Math.sqrt(2);

    private AbstractWaypointSprite sprite;
    private AbstractWaypointSprite leader;
    private Vector offset;

    private Vector cOffset = new Vector();
    private Vector oldPos;
    private List<Position> nodes;

    public LeaderFollow(AbstractWaypointSprite sprite, AbstractWaypointSprite leader, Vector offset) {
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
        switch (leader.getDirection()) {
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

        Vector desired = target.subtract(sprite.getPositionVector());
        desired = desired.truncate(MAX_VELOCITY);

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * sprite.getMovementSpeed());
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * sprite.getMovementSpeed());

        if (nodes.size() == 0) {
            if (Math.abs(sprite.getX() - target.x) <= 4.5f && Math.abs(sprite.getY() - target.y) <= 4.5f) {
                sprite.setSteeringVelocity(new Vector(0, 0));
                sprite.setFacing(leader.getDirection());
            }
        }
    }

    public List<Position> getNodes() {
        return nodes;
    }
}
