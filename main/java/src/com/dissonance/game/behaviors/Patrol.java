package com.dissonance.game.behaviors;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.ai.behaviors.FiniteBehavior;
import com.dissonance.framework.game.ai.behaviors.PathFollow;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.util.Random;

public class Patrol implements Behavior {
    private static final Random random = new Random();
    private AbstractWaypointSprite owner;
    private Direction direction;

    public Patrol(AbstractWaypointSprite owner) {
        this.owner = owner;
        this.direction = owner.getFacingDirection();
    }

    @Override
    public void update() {
        Position pos = directionToPosition(true);
        WaypointLikeSeek follow = new WaypointLikeSeek(owner, pos);
        follow.setOnFinishedListener(ON_FINISHED);

        owner.setBehavior(follow);
    }

    private final FiniteBehavior.FiniteBehaviorEvent.OnFinished ON_FINISHED = new FiniteBehavior.FiniteBehaviorEvent.OnFinished() {
        @Override
        public void onFinished(FiniteBehavior behavior) {
            owner.setBehavior(null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(3000) + 1000L);
                        direction = direction.opposite();
                        owner.setBehavior(Patrol.this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };

    private static final int TILE_DISTANCE = 10;
    private Position directionToPosition(boolean recursive) {
        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                float distance = TILE_DISTANCE * 16f;
                if (owner instanceof PhysicsSprite) {
                    PhysicsSprite physicsSprite = (PhysicsSprite)owner;
                    for (HitBox hb : physicsSprite.getHitBoxes()) {
                        while (hb.checkForCollision(owner, owner.getX(), owner.getY() + distance)) {
                            distance--;
                            if (distance <= 0 && recursive) {
                                direction = direction.opposite();
                                return directionToPosition(false);
                            } else if (distance <= 0)
                                return new Position(owner.getX(), owner.getY());
                        }
                    }
                }
                return new Position(owner.getX(), owner.getY() + distance);
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                distance = TILE_DISTANCE * 16f;
                if (owner instanceof PhysicsSprite) {
                    PhysicsSprite physicsSprite = (PhysicsSprite)owner;
                    for (HitBox hb : physicsSprite.getHitBoxes()) {
                        while (hb.checkForCollision(owner, owner.getX(), owner.getY() - distance)) {
                            distance--;
                            if (distance <= 0 && recursive) {
                                direction = direction.opposite();
                                return directionToPosition(false);
                            } else if (distance <= 0)
                                return new Position(owner.getX(), owner.getY());
                        }
                    }
                }
                return new Position(owner.getX(), owner.getY() - distance);
            case LEFT:
                distance = TILE_DISTANCE * 16f;
                if (owner instanceof PhysicsSprite) {
                    PhysicsSprite physicsSprite = (PhysicsSprite)owner;
                    for (HitBox hb : physicsSprite.getHitBoxes()) {
                        while (hb.checkForCollision(owner, owner.getX() - distance, owner.getY())) {
                            distance--;
                            if (distance <= 0 && recursive) {
                                direction = direction.opposite();
                                return directionToPosition(false);
                            } else if (distance <= 0)
                                return new Position(owner.getX(), owner.getY());
                        }
                    }
                }
                return new Position(owner.getX() - distance, owner.getY());
            case RIGHT:
                distance = TILE_DISTANCE * 16f;
                if (owner instanceof PhysicsSprite) {
                    PhysicsSprite physicsSprite = (PhysicsSprite)owner;
                    for (HitBox hb : physicsSprite.getHitBoxes()) {
                        while (hb.checkForCollision(owner, owner.getX() + distance, owner.getY())) {
                            distance--;
                            if (distance <= 0 && recursive) {
                                direction = direction.opposite();
                                return directionToPosition(false);
                            } else if (distance <= 0)
                                return new Position(owner.getX(), owner.getY());
                        }
                    }
                }
                return new Position(owner.getX() + distance, owner.getY());
            case NONE:
            case MOVING:
            default:
                return new Position(owner.getX(), owner.getY());
        }
    }
}
