package com.dissonance.framework.game.ai.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.FiniteBehavior.FiniteBehaviorEvent;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;

import java.awt.*;
import java.util.Random;

public final class Idle implements Behavior {

    private static Random random = new Random();

    private AbstractWaypointSprite sprite;
    private volatile boolean shouldUpdate = true;

    private FiniteBehaviorEvent.OnFinished onFinished = new FiniteBehaviorEvent.OnFinished() {
        @Override
        public void onFinished(FiniteBehavior behavior) {
            new Thread(new Runnable() {
                String[] rand = new String[] {
                        "Could you please shove a broom up my ass?",
                        "I eat dicks for breakfast.",
                        "such msg wow",
                        "scootaloo is the best poni"
                };
                public void run() {
                    if (sprite.getBehavior() != null && sprite.getBehavior() instanceof Approach) {
                        sprite.toastText(rand[random.nextInt(rand.length)], 5000, Color.RED);
                    }
                    sprite.setBehavior(null);
                    try {
                        Thread.sleep(random.nextInt(3500) + 7500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sprite.setBehavior(Idle.this);
                }
            }).start();
        }
    };

    public Idle(AbstractWaypointSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void update() {
        if (!shouldUpdate) {
            return;
        }

        if (random.nextInt(100) > 49) {
            System.out.println("Approach");
            Approach approach = new Approach(sprite, Approach.getRandomTarget(sprite));
            approach.setOnFinishedListener(onFinished);
            sprite.setBehavior(approach);
        } else {
            float radius = 320;
            float minX = sprite.getX() - radius;
            minX = minX < 0f ? 0f : minX;
            float maxX = sprite.getX() + radius;
            maxX = maxX > sprite.getWorld().getPixelWidth() ? sprite.getWorld().getPixelWidth() : maxX;
            float minY = sprite.getY() - radius;
            minY = minY < 0f ? 0f : minY;
            float maxY = sprite.getY() + radius;
            maxY = maxY > sprite.getWorld().getPixelHeight() ? sprite.getWorld().getPixelHeight() : maxX;

            float randX = random.nextFloat() * (maxX - minX + 1) + minX;
            float randY = random.nextFloat() * (maxY - minY + 1) + minY;
            System.out.println("PathFollow to " + randX + " " + randY);
            PathFollow follow = new PathFollow(sprite, new Position(randX, randY));
            follow.setOnFinishedListener(onFinished);
            sprite.setBehavior(follow);
        }
    }
}
