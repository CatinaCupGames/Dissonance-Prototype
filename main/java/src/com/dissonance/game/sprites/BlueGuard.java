package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.*;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.behaviors.WaypointLikeIdle;
import com.dissonance.game.behaviors.WaypointLikeSeek;

public class BlueGuard extends Enemy {
    public BlueGuard() {
        super("farrand", StatType.NON_MAGIC, CombatType.HUMAN);
    }


    @Override
    public void onMovement(Direction direction) {
        if (isAttacking())
            return;
        if (isAnimationPaused()) {
            super.setFrame(2);
            playAnimation();
        }

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

    @Override
    public void onNoMovement() {
        if (isMoving() || isAttacking()) {
            return;
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
        setSpeed(6);
        setVigor(8);
        setStamina(4);
        setMarksmanship(8);
    }

    @Override
    public void strike(CombatSprite attacker, WeaponItem with) {
        super.strike(attacker, with);

        if (getClosestPlayer() == attacker && getHP() < getMaxHP() / 3.0) {
            run = !run;
        }
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        runAI();
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return sprite instanceof BlueGuard || sprite instanceof RedGuard;
    }


    private boolean run;
    private boolean idle;
    private long lastAttack;
    private boolean looking = false;
    private long foundTime = 0L;
    private static final long ATTACK_RATE_MS = 1800;
    private static final long FOUND_YOU_MS = 400;
    private void runAI() {
        if (getCurrentWeapon() == null || run) {
            setMovementSpeed(14f);
            if (getBehavior() == null || !(getBehavior() instanceof Flee)) {
                PlayableSprite sprite = getClosestPlayer();
                if (sprite == null) return;

                Flee flee = new Flee(this, sprite, (getMarksmanship() * 2f) * 16f);
                setBehavior(flee);
                flee.setFleeListener(FLEE_LISTENER);
            } else if (getBehavior() != null) {
                Flee flee = (Flee)getBehavior();

                PlayableSprite sprite = getClosestPlayer();
                if (sprite == null) return;
                flee.setTarget(sprite);
            }
        } else {
            setMovementSpeed(10f);
            PlayableSprite sprite = getClosestPlayer();
            if (sprite == null) {
                if (!idle) {
                    idle = true;
                    WaypointLikeIdle idleBehavior = new WaypointLikeIdle(this, 720);
                    setBehavior(idleBehavior);
                }
            } else {
                idle = false;
                if (distanceFrom(sprite) <= getCurrentWeapon().getWeaponInfo().getRange() + (sprite.getWidth() / 2f)) {
                    if (looking) {
                        foundTime = System.currentTimeMillis();
                        looking = false;
                    }

                    if (isAttacking()) return;
                    if (System.currentTimeMillis() - lastAttack < ATTACK_RATE_MS) return;
                    if (System.currentTimeMillis() - foundTime < FOUND_YOU_MS) return;

                    lastAttack = System.currentTimeMillis();
                    getCurrentWeapon().use("swipe");
                    setBehavior(null);
                } else {
                    looking = true;
                    Behavior behavior = getBehavior();
                    if (behavior instanceof Seek) {
                        WaypointLikeSeek seek = (WaypointLikeSeek)getBehavior();
                        seek.setTarget(getSeekTarget(sprite));
                    } else {
                        WaypointLikeSeek seek = new WaypointLikeSeek(this, getSeekTarget(sprite));
                        setBehavior(seek);
                    }
                }
            }
        }
    }

    private final Flee.FleeListener FLEE_LISTENER = new Flee.FleeListener() {
        @Override
        public void onSpriteSafe(AbstractWaypointSprite sprite) {
            run = false;
            setBehavior(null);
        }
    };

    private PlayableSprite getClosestPlayer() {
        float distance = getMarksmanship() * 16f;

        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        PlayableSprite closet = null;
        float dis = 0;
        for (PlayableSprite sprite : sprites) {
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
