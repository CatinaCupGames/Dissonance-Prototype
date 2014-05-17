package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.RenderService;

/**
 * Flee is the exact opposite of the {@link Seek} behavior. It will make
 * the sprite run away from the target sprite. It's also possible to specify
 * the minimal distance needed between the two sprites so that the sprite starts
 * running away.
 */
public final class Flee implements Behavior {
    private final AbstractWaypointSprite sprite;
    private AbstractWaypointSprite target;
    private float panicDistance = -1f;
    private FleeListener listener;

    public Flee(AbstractWaypointSprite sprite, AbstractWaypointSprite target) {
        this.sprite = sprite;
        this.target = target;
    }

    public Flee(AbstractWaypointSprite sprite, AbstractWaypointSprite target, float panicDistance) {
        this(sprite, target);
        this.panicDistance = panicDistance * panicDistance;
    }

    public void setFleeListener(FleeListener listener) {
        this.listener = listener;
    }

    @Override
    public void update() {
        if (target == null) { //uwot
            sprite.setBehavior(null);
            return;
        }

        Vector desired = sprite.getPositionVector().subtract(target.getPositionVector());

        if (desired.lengthSquared() > panicDistance && panicDistance != -1f) {
            if (listener != null)
                listener.onSpriteSafe(sprite);
            return;
        }

        desired = desired.truncate(MAX_VELOCITY);

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * sprite.getMovementSpeed());
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * sprite.getMovementSpeed());
    }

    public void setPanicDistance(float panicDistance) {
        this.panicDistance = panicDistance * panicDistance;
    }

    public void setTarget(AbstractWaypointSprite target) {
        this.target = target;
    }

    public AbstractWaypointSprite getTarget() {
        return target;
    }

    public static interface FleeListener {
        public void onSpriteSafe(AbstractWaypointSprite sprite);
    }
}
