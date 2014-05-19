package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.FiniteBehavior.FiniteBehaviorEvent;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

import java.util.Random;

public final class Idle implements Behavior {

    private static final Random random = new Random();

    private AbstractWaypointSprite sprite;
    private final float radius;
    private final float[] limits;


    private FiniteBehaviorEvent.OnFinished onFinished = new FiniteBehaviorEvent.OnFinished() {
        @Override
        public void onFinished(FiniteBehavior behavior) {
            new Thread(new Runnable() {
                public void run() {
                    sprite.setBehavior(null);
                    try {
                        Thread.sleep(random.nextInt(3500) + 5500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sprite.setBehavior(Idle.this);
                }
            }).start();
        }
    };

    public Idle(AbstractWaypointSprite sprite) {
        this(sprite, -1);
    }

    public Idle(AbstractWaypointSprite sprite, float radius) {
        this.sprite = sprite;
        this.radius = radius;

        float minX = sprite.getX() - radius;
        minX = minX < 0f ? 0f : minX;
        float maxX = sprite.getX() + radius;
        maxX = maxX > sprite.getWorld().getPixelWidth() ? sprite.getWorld().getPixelWidth() : maxX;
        float minY = sprite.getY() - radius;
        minY = minY < 0f ? 0f : minY;
        float maxY = sprite.getY() + radius;
        maxY = maxY > sprite.getWorld().getPixelHeight() ? sprite.getWorld().getPixelHeight() : maxX;

        limits = new float[] { minX, maxX, minY, maxY };
    }

    @Override
    public void update() {
        if (random.nextInt(100) > 49) {
            AbstractWaypointSprite target;
            target = radius == -1 ? Approach.getRandomTarget(sprite) : Approach.getRandomTarget(sprite, limits);
            Approach approach = new Approach(sprite, target);
            approach.setOnFinishedListener(onFinished);
            sprite.setBehavior(approach);
        } else {
            float randX;
            float randY;

            if (radius == -1) {
                float tw = sprite.getWorld().getTiledData().getTileWidth();
                float th = sprite.getWorld().getTiledData().getTileHeight();

                float xs = sprite.getX() - radius;
                xs = (xs < 0 ? 0 : xs);
                float xe = sprite.getX() + radius;
                xe = (xe > sprite.getWorld().getWidth() * tw ? sprite.getWorld().getWidth() * tw : xe);
                float ys = sprite.getY() - radius;
                ys = (ys < 0 ? 0 : ys);
                float ye = sprite.getY() + radius;
                ye = (ye > sprite.getWorld().getHeight() * th ? sprite.getWorld().getHeight() * th : ye);
                randX = random.nextFloat() * (xe - xs + 1) + xs;
                randY = random.nextFloat() * (ye - ys + 1) + ys;
            } else {
                randX = random.nextFloat() * (limits[1] - limits[0] + 1) + limits[0];
                randY = random.nextFloat() * (limits[3] - limits[2] + 1) + limits[2];
            }

            PathFollow follow = new PathFollow(sprite, new Position(randX, randY));
            follow.setOnFinishedListener(onFinished);
            sprite.setBehavior(follow);
        }
    }
}
