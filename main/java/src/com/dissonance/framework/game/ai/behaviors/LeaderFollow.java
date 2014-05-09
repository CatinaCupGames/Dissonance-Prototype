package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

import java.util.List;

public final class LeaderFollow implements Behavior {

    static final float RECALCULATE_DISTANCE = 15f;
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

        switch (leader.getDirection().simple()) {
            case UP:
                cOffset.x = offset.x;
                cOffset.y = offset.y;
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

        NodeMap map = sprite.getWorld().getNodeMap();
        int tw = sprite.getWorld().getTiledData().getTileWidth();
        int th = sprite.getWorld().getTiledData().getTileHeight();
        Position ps = new Position(leader.getPositionVector().add(cOffset));
        nodes = map.findPath(sprite.getPosition().shrink(tw, th), ps.shrink(tw, th));
        nodes.remove(0);
    }

    public boolean shouldRecalculate() {
        Vector pos = leader.getPositionVector();
        Vector newPos;
        newPos = pos.subtract(oldPos);
        boolean rc = (Math.abs(newPos.x) >= RECALCULATE_DISTANCE || Math.abs(newPos.y) >= RECALCULATE_DISTANCE);

        if (rc) {
            oldPos = pos;
        }

        return rc;
    }

    @Override
    public void update() {
        if (shouldRecalculate()) {
            NodeMap map = sprite.getWorld().getNodeMap();
            int tw = sprite.getWorld().getTiledData().getTileWidth();
            int th = sprite.getWorld().getTiledData().getTileHeight();

            switch (leader.getDirection().simple()) {
                case UP:
                    cOffset.x = offset.x;
                    cOffset.y = offset.y;
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

            Position ps = new Position(leader.getPositionVector().add(cOffset));
            nodes = map.findPath(sprite.getPosition().shrink(tw, th), ps.shrink(tw, th));
            nodes.remove(0);
        }
        Vector target = null;

        if (nodes != null && nodes.size() >= 1) {
            target = nodes.get(0).vector().multiply(16f);

            if (sprite.getPositionVector().subtract(target).length() <= 10) {
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
            sprite.setSteeringVelocity(new Vector());
        }
    }

    public List<Position> getNodes() {
        return nodes;
    }
}
