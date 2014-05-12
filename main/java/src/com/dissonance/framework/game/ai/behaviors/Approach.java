package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Approach is a behavior that makes the sprite approach and face the specified target
 * making it look like they're talking to each other. <br />
 * You can use the {@link Approach#getRandomTarget(AbstractWaypointSprite, float)} to
 * get a random target to approach.
 */
public final class Approach implements FiniteBehavior {

    private static final Vector offset = new Vector(0, -32);
    private static final float SQRT2 = (float) Math.sqrt(2);

    private AbstractWaypointSprite sprite;
    private AbstractWaypointSprite target;

    private Vector cOffset = new Vector();
    private static Random random = new Random();
    private List<Position> nodes = new ArrayList<>();

    private FiniteBehaviorEvent.OnFinished onFinishedListener;

    public Approach(AbstractWaypointSprite sprite, AbstractWaypointSprite target) {
        this.sprite = sprite;
        this.target = target;
        calculateNodes();
    }

    @Override
    public void update() {
        if (target == null) {
            if (onFinishedListener != null) {
                onFinishedListener.onFinished(this);
            }
            return;
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
            if (onFinishedListener != null) {
                onFinishedListener.onFinished(this);
            }
            return;
        }

        Vector desired = target.subtract(sprite.getPositionVector());
        desired = desired.normalize();

        float distance = this.target.getPositionVector().subtract(sprite.getPositionVector()).length();
        if (distance <= 160f) {
            desired = desired.multiply(MAX_VELOCITY * distance / 160f);
        } else {
            desired = desired.multiply(MAX_VELOCITY);
        }

        Vector steering = desired.subtract(sprite.getSteeringVelocity());
        steering = steering.truncate(MAX_FORCE).multiply(0.21186f);

        sprite.setSteeringVelocity(sprite.getSteeringVelocity().add(steering).truncate(MAX_VELOCITY));

        sprite.setX(sprite.getX() + sprite.getSteeringVelocity().x * RenderService.TIME_DELTA * sprite.getMovementSpeed());
        sprite.setY(sprite.getY() + sprite.getSteeringVelocity().y * RenderService.TIME_DELTA * sprite.getMovementSpeed());

        if (nodes.size() == 0) {
            if (Math.abs(sprite.getX() - target.x) <= 4.5f && Math.abs(sprite.getY() - target.y) <= 4.5f) {
                sprite.setSteeringVelocity(new Vector(0, 0));

                if (onFinishedListener != null) {
                    onFinishedListener.onFinished(this);
                }

                //TODO: set direction opposite of target's
            }
        }
    }

    public static AbstractWaypointSprite getRandomTarget(AbstractWaypointSprite base) {
        return getRandomTarget(base, -1f);
    }

    public static AbstractWaypointSprite getRandomTarget(AbstractWaypointSprite base, float radius) {
        float[] bounds;

        float tw = base.getWorld().getTiledData().getTileWidth();
        float th = base.getWorld().getTiledData().getTileHeight();

        if (radius == -1) {
            bounds = new float[] { 0, base.getWorld().getWidth() * tw, 0, base.getWorld().getHeight() * th };
        } else {
            if (radius < 0) {
                throw new IllegalArgumentException("Radius must be positive!");
            }

            float xs = base.getX() - radius;
            xs = (xs < 0 ? 0 : xs);
            float xe = base.getX() + radius;
            xe = (xe > base.getWorld().getWidth() * tw ? base.getWorld().getWidth() * tw : xe);
            float ys = base.getY() - radius;
            ys = (ys < 0 ? 0 : ys);
            float ye = base.getY() + radius;
            ye = (ye > base.getWorld().getHeight() * th ? base.getWorld().getHeight() * th : ye);

            bounds = new float[] { xs, xe, ys, ye };
        }

        return getRandomTarget(base, bounds);
    }

    public static AbstractWaypointSprite getRandomTarget(AbstractWaypointSprite base, float[] bounds) {
        Iterator<UpdatableDrawable> drawables = base.getWorld().getUpdatables();
        List<AbstractWaypointSprite> inRange = new ArrayList<>();

        while(drawables.hasNext()) {
            UpdatableDrawable next = drawables.next();

            if (!(next instanceof AbstractWaypointSprite)) {
                continue;
            }

            AbstractWaypointSprite s = (AbstractWaypointSprite) next;
            if (s.getPositionVector().inRange(bounds[0], bounds[1], bounds[2], bounds[3]) && !s.equals(base)) {
                inRange.add((AbstractWaypointSprite) next);
            }
        }

        if (inRange.size() == 0) {
            return null;
        }

        return inRange.get(random.nextInt(inRange.size()));
    }

    public FiniteBehaviorEvent.OnFinished getOnFinishedListener() {
        return onFinishedListener;
    }

    public void setOnFinishedListener(FiniteBehaviorEvent.OnFinished onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }

    private void calculateNodes() {
        NodeMap map = sprite.getWorld().getNodeMap();
        int tw = sprite.getWorld().getTiledData().getTileWidth();
        int th = sprite.getWorld().getTiledData().getTileHeight();

        if (target == null) {
            return;
        }

        calculateCOffset();

        Position ps = new Position(target.getPositionVector().add(cOffset));
        nodes = map.findPath(sprite.getPosition().shrink(tw, th), ps.shrink(tw, th));
        nodes.remove(0);
    }

    private void calculateCOffset() {
        switch (target.getDirection()) {
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
}
