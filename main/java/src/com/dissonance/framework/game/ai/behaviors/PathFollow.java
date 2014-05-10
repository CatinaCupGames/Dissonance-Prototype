package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

import java.util.List;

/**
 * PathFollow is a steering behavior that uses A* to move towards the path, avoiding any
 * obstacles in the process by creating intermediate points to move to. The sprite will move
 * to these points using the {@link Seek} behavior, producing a smooth movement and avoiding
 * sudden route changes.
 */
public final class PathFollow implements Behavior {

    private AbstractWaypointSprite sprite;
    private List<Position> nodes;

    public PathFollow(AbstractWaypointSprite sprite, Position path) {
        this.sprite = sprite;
        NodeMap map = sprite.getWorld().getNodeMap();
        int tw = sprite.getWorld().getTiledData().getTileWidth();
        int th = sprite.getWorld().getTiledData().getTileHeight();
        nodes = map.findPath(new Position(sprite.getX(), sprite.getY()).shrink(tw, th), path.shrink(tw, th));
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
            return;
        }

        Vector desired = target.subtract(sprite.getPositionVector());
        desired = desired.truncate(MAX_VELOCITY);

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        //TODO: change the *10 part if we ever make speed sprite-dependent
        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * 10);
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * 10);
    }

    public List<Position> getNodes() {
        return nodes;
    }
}
