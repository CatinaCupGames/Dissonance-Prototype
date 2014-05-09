package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;

public final class Arrive implements Behavior {

    static final float SLOWING_RADIUS = 200;

    private AbstractWaypointSprite sprite;
    private Vector target;

    public Arrive(AbstractWaypointSprite sprite, Vector target) {
        this.sprite = sprite;
        this.target = target;
    }

    @Override
    public void update() {

        Vector desired = target.subtract(sprite.getPositionVector());
        float distance = desired.length();
        desired = desired.normalize();

        if (distance <= Arrive.SLOWING_RADIUS) {
            desired = desired.multiply(Behavior.MAX_VELOCITY * distance / Arrive.SLOWING_RADIUS);
        } else {
            desired = desired.multiply(Behavior.MAX_VELOCITY);
        }

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        float c = RenderService.TIME_DELTA * sprite.getMovementSpeed();
        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * c);
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * c);

        if (Math.abs(sprite.getX() - target.x) <= 4.5f && Math.abs(sprite.getY() + sprite.getHeight() / 2 - target.y) <= 4.5f) {
            sprite.setBehavior(null);
            sprite.setSteeringVelocity(new Vector(0, 0));
        }
    }
}
