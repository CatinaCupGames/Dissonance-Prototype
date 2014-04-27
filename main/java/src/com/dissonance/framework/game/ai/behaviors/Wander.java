package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

/**
 * Wander is a steering behavior that makes the target sprite
 * move around the screen randomly by changing it's angle by
 * a very small amount every update. This should make the
 * sprite wander around smoothly.
 */
public final class Wander implements Behavior {
    private static final float MAX_WANDER_FORCE = 0.45f;
    private static final float MAX_WANDER_VELOCITY = 0.25f;
    private static final float CIRCLE_DISTANCE = 6;
    private static final float CIRCLE_RADIUS = 8;
    private static final float ANGLE_CHANGE = 1;
    private float wanderAngle = 0f;

    private AbstractWaypointSprite sprite;

    public Wander(AbstractWaypointSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void update() {
        Vector center = new Vector(sprite.getSteeringVelocity());
        center.truncate(CIRCLE_DISTANCE);

        Vector displacement = new Vector(0, -1).multiply(CIRCLE_RADIUS).angle(wanderAngle);
        wanderAngle += Math.random() * ANGLE_CHANGE - (ANGLE_CHANGE * 0.5);

        Vector steering = center.add(displacement).truncate(MAX_WANDER_FORCE).multiply(0.21186f);
        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_WANDER_VELOCITY));

        //TODO: change the *10 part if we ever make speed sprite-dependent
        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * 10);
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * 10);
    }
}
