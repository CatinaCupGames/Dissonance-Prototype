package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.ai.behaviors.Flee;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.game.GameCache;
import com.dissonance.game.behaviors.Patrol;
import com.dissonance.game.behaviors.Search;
import com.dissonance.game.behaviors.WaypointLikeSeek;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.w.FactoryFloorCat;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BlueGuard extends Enemy {
    public BlueGuard() {
        super("meleeguard", StatType.NON_MAGIC, CombatType.HUMAN);
    }
    private boolean isHostile = true;

    @Override
    public void onMovement(Direction direction) {
        if (BossQuest.END) {
            setAnimation("walk_right");
            super.setFrame(1);
            pauseAnimation();
            return;
        }
        if (isAttacking() || isDieing || fallOver)
            return;
        if (isAnimationPaused()) {
            super.setFrame(2);
            playAnimation();
        }

        String type = "walk";
        if (getMovementType() == MovementType.RUNNING)
            type = "run";

        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                setAnimation(type + "_back");
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                setAnimation(type + "_front");
                break;
            case LEFT:
                setAnimation(type + "_left");
                break;
            case RIGHT:
                setAnimation(type + "_right");
                break;
        }
    }

    @Override
    public void onNoMovement() {
        if (BossQuest.END) {
            setAnimation("walk_right");
            super.setFrame(1);
            pauseAnimation();
            return;
        }
        if (isMoving() || isAttacking() || isDieing || fallOver) {
            return;
        }
        if (getMovementType() == MovementType.RUNNING) {
            switch (direction) {
                case UP:
                case UP_LEFT:
                case UP_RIGHT:
                    setAnimation("walk_back");
                    break;
                case DOWN:
                case DOWN_LEFT:
                case DOWN_RIGHT:
                    setAnimation("walk_front");
                    break;
                case LEFT:
                    setAnimation("walk_left");
                    break;
                case RIGHT:
                    setAnimation("walk_right");
                    break;
            }
        }
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        pauseAnimation();

        setCurrentWeapon(Weapon.getWeapon("guardsword").createItem(this));
        setAttack(10);
        setDefense(8);
        setSpeed(4);
        setVigor(8);
        setStamina(60);
        setMaxStamina(60);
        setMarksmanship(8);
        setMovementSpeed(1f);
    }

    public boolean fallOver;
    public void fallOver(final Runnable toRun) {
        fallOver = true;
        setInvincible(true);
        setHostile(false);
        Direction direction1 = getFacingDirection();
        if (direction1 == Direction.RIGHT)
            setAnimation("die_right");
        else if (direction1 == Direction.LEFT)
            setAnimation("die_left");
        else
            setAnimation(random.nextBoolean() ? "die_right" : "die_left");
        if (toRun != null) {
            addAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    toRun.run();
                    removeAnimationFinishedListener(this);
                }
            });
        }
        playAnimation();
    }

    private boolean isDieing = false;
    @Override
    public void onDeath() {
        isDieing = true;
        fallOver(new Runnable() {
            @Override
            public void run() {
                blink();
            }
        });
    }

    @Override
    public void onBlink() {
        super.onBlink();
        if (isInvincible()) {
            super.onDeath();
        }
    }

    @Override
    public void strike(CombatSprite attacker, WeaponItem with) {
        super.strike(attacker, with);

        if (target == attacker && getHP() < getMaxHP() / 3.0) {
            run = true;
        }
    }

    @Override
    public void update() {
        if (Camera.isOffScreen(getX(), getY(), getWidth(), getHeight()))
            return;
        if (getWorld().equals(GameCache.FactoryFloor)) {
            //Ensure we always use the correct node map
            if (getLayer() == 2)
                getWorld().setActiveNodeMap(FactoryFloorCat.groundNodeMap);
            else if (getLayer() == 6)
                getWorld().setActiveNodeMap(FactoryFloorCat.nongroundNodeMap);
        }

        super.update();
        if (isUpdateCanceled())
            return;
        if(isHostile) {
            runAI();
        }

        //And be sure to reset it
        getWorld().setActiveNodeMap(getWorld().getDefaultNodeMap());
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return sprite instanceof BlueGuard || sprite instanceof RedGuard;
    }

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean isHostile) {
        this.isHostile = isHostile;
    }

    @Override
     protected void onAnimationFinished() {
        if (getCurrentAnimation().getName().startsWith("swipe")) {
            if (run && !isDodging() && target != null) {
                Timer.delayedInvokeRunnable(100, new Runnable() {
                    @Override
                    public void run() {
                        dodge(directionTowards(target).opposite());
                    }
                });
            }
        }
        super.onAnimationFinished();
    }

    private int movementSpeed() {
        return 15 + (getSpeed());
    }


    private boolean run;
    private boolean idle;
    private long lastAttack;
    private boolean looking = false;
    private boolean dodgeAway = false;
    private boolean saw = false;
    private long foundTime = 0L;
    private PlayableSprite target;
    private static final long ATTACK_RATE_MS = 900;
    private static final long FOUND_YOU_MS = 400;
    private static final long SPOT_TIME = 2000;
    private static final Random random = new Random();
    private void runAI() {
        if (isDodging())
            return;
        if (getCurrentWeapon() != null) {
            target = getClosestPlayer();
            if (target == null) {
                if (getBehavior() != null && getBehavior() instanceof Search) {
                    face(((Search)getBehavior()).getOrginalDirection());
                }
                saw = false;
                if (!idle) {
                    setMovementSpeed(movementSpeed() / 4f);
                    idle = true;
                    setMovementType(MovementType.WALKING);
                    Patrol patrol = new Patrol(this);
                    setBehavior(patrol);
                }
            } else {
                setMovementType(MovementType.RUNNING);
                if (!saw && !isPlayerSeen(target)) {
                    if (getBehavior() == null || !(getBehavior() instanceof Search)) {
                        Search search = new Search(this);
                        setBehavior(search);
                    }
                    return;
                }
                if (!saw) {
                    toastText("!")
                            .setToastFontSize(32f)
                            .setTint(Color.RED);
                }
                saw = true;
                spot.clear();
                setMovementSpeed(movementSpeed() / 1.5f);
                idle = false;
                if (distanceFrom(target) <= 3 + getCurrentWeapon().getWeaponInfo().getRange() + (target.getWidth() / 4f)) {
                    if (looking) {
                        foundTime = System.currentTimeMillis();
                        looking = false;
                    }

                    if (isAttacking()) return;
                    if (System.currentTimeMillis() - lastAttack < ATTACK_RATE_MS) {
                        dodgeAway = true;
                        dodgeAway();
                        return;
                    }
                    if (System.currentTimeMillis() - foundTime < FOUND_YOU_MS) return;

                    lastAttack = System.currentTimeMillis();
                    getCurrentWeapon().use("swipe");
                    setBehavior(null);

                } else {
                    if (dodgeAway && !isDodging()) {
                        dodgeAway();
                        dodgeAway = false;
                        return;
                    }
                    looking = true;
                    Behavior behavior = getBehavior();
                    if (behavior instanceof WaypointLikeSeek) {
                        WaypointLikeSeek seek = (WaypointLikeSeek)getBehavior();
                        seek.setTarget(getSeekTarget(target));
                    } else {
                        WaypointLikeSeek seek = new WaypointLikeSeek(this, getSeekTarget(target));
                        setBehavior(seek);
                    }
                }
            }
        }
    }

    private void dodgeAway() {
        Direction direction = directionTowards(target).rotate90();
        int rnd = random.nextInt(3);
        switch (rnd) {
            case 0:
                dodge(direction);
                break;
            case 1:
                dodge(direction.rotate90());
                break;
            case 2:
                dodge(direction.rotateNegitive90());
        }
    }

    private boolean isPlayerSeen(PlayableSprite target) {
        if (getLayer() != target.getLayer()) {
            if (!spot.containsKey(target)) {
                spot.put(target, System.currentTimeMillis());
                toastText("?")
                        .setToastFontSize(32f)
                        .setTint(Color.RED);
            }
            return false;
        }
        else if (directionTowards(target) != getFacingDirection()) {
            if (directionTowards(target) != getFacingDirection().opposite()) {
                if (!spot.containsKey(target)) {
                    spot.put(target, System.currentTimeMillis());
                    toastText("?")
                            .setToastFontSize(32f)
                            .setTint(Color.RED);
                    return false;
                }
                long l = spot.get(target);
                if (System.currentTimeMillis() - l > SPOT_TIME) {
                    spot.remove(target);
                    return true;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    private final Flee.FleeListener FLEE_LISTENER = new Flee.FleeListener() {
        @Override
        public void onSpriteSafe(AbstractWaypointSprite sprite) {
            run = false;
            setBehavior(null);
        }
    };

    private HashMap<PlayableSprite, Long> spot = new HashMap<>();
    private Direction oDirection;
    private PlayableSprite getClosestPlayer() {
        float distance = 15f * 16f;

        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        PlayableSprite closet = null;
        float dis = 0;
        for (PlayableSprite sprite : sprites) {
            if (sprite.isDead() || !sprite.isVisible())
                continue;
            float temp = (float) Math.sqrt(((getX() - sprite.getX()) * (getX() - sprite.getX())) + ((getY() - sprite.getY()) * (getY() - sprite.getY())));
            if (temp <= distance) {
                if (temp < dis || closet == null) {
                    dis = temp;
                    closet = sprite;
                }
            }
        }

        return closet;
    }



    static HashMap<PlayableSprite, ArrayList<CombatSprite>> attackers = new HashMap<>();
    private Position getSeekTarget(Sprite sprite) {
        float x = sprite.getX();
        float y = sprite.getY();
/*
        Direction direction1 = directionTowards(sprite);
        switch (direction1) {
            case UP:
                y += (sprite.getHeight() / 4f) + (getCurrentWeapon().getWeaponInfo().getRange() / 4f);
                break;
            case DOWN:
                y -= (sprite.getHeight() / 4f) + (getCurrentWeapon().getWeaponInfo().getRange() / 4f);
                break;
            case LEFT:
                x += (sprite.getWidth() / 4f) + (getCurrentWeapon().getWeaponInfo().getRange() / 4f);
                break;
            case RIGHT:
                x -= (sprite.getHeight() / 4f) + (getCurrentWeapon().getWeaponInfo().getRange() / 4f);
                break;
        }
*/

        return new Position(x, y);
    }
}
