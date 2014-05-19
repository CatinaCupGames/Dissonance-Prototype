package com.dissonance.game.behaviors;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.FiniteBehavior;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WaypointLikeApproach implements FiniteBehavior {

    private static final Vector offset = new Vector(0, -32);
    private static final float SQRT2 = (float) Math.sqrt(2);

    private AbstractWaypointSprite sprite;
    private AbstractWaypointSprite target;

    private Vector cOffset = new Vector();
    private static Random random = new Random();
    private List<Position> nodes = new ArrayList<>();

    private FiniteBehavior.FiniteBehaviorEvent.OnFinished onFinishedListener;

    public WaypointLikeApproach(AbstractWaypointSprite sprite, AbstractWaypointSprite target) {
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

        float xdiff = sprite.getX() - target.x;
        float ydiff = sprite.getY() - target.y;
        float speed = sprite.getMovementSpeed();
        boolean moved = false;

        float difx = 4f;
        float dify = 4f;

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

        if (nodes.size() == 0) {
            if (Math.abs(sprite.getX() - target.x) <= 4.5f && Math.abs(sprite.getY() - target.y) <= 4.5f) {
                if (onFinishedListener != null) {
                    onFinishedListener.onFinished(this);
                }

                this.target.face(sprite.getFacingDirection().opposite());
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
                if (base instanceof CombatSprite && next instanceof CombatSprite) {
                    if (!((CombatSprite)next).isAlly((CombatSprite)base)) //Don't approach non-allies
                        continue;
                }
                inRange.add((AbstractWaypointSprite) next);
            }
        }

        if (inRange.size() == 0) {
            return null;
        }

        return inRange.get(random.nextInt(inRange.size()));
    }

    public FiniteBehavior.FiniteBehaviorEvent.OnFinished getOnFinishedListener() {
        return onFinishedListener;
    }

    public void setOnFinishedListener(FiniteBehavior.FiniteBehaviorEvent.OnFinished onFinishedListener) {
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
        switch (target.getFacingDirection()) {
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