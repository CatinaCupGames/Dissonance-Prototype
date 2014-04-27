package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

/**
 * Seek is a steering behavior that will make the sprite move towards the target
 * smoothly. If the sprite is at rest it will just move towards the target, however
 * if it already has a steering velocity, it will gradually change the angle and turn
 * towards the target, adjusting the velocity smoothly and therefore avoiding sudden
 * route changes.
 */
public final class Seek implements Behavior {
    private final AbstractWaypointSprite sprite;
    private Vector target;

    public Seek(AbstractWaypointSprite sprite, Position target) {
        this.sprite = sprite;
        this.target = new Vector(target.getX(), target.getY());
    }

    @Override
    public void update() {
        Vector desired = target.subtract(sprite.getPositionVector());
        desired = desired.truncate(MAX_VELOCITY);

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        //TODO: change the *10 part if we ever make speed sprite-dependent
        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * 10);
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * 10);

        if (Math.abs(sprite.getX() - target.x) <= 4.5f && Math.abs(sprite.getY() - target.y) <= 4.5f) {
            sprite.setBehavior(null);
            sprite.setSteeringVelocity(new Vector(0, 0));
        }
    }

    public void setTarget(Vector target) {
        this.target = target;
    }

    public void setTarget(Position position) {
        this.target = position.vector();
    }
}
