package com.dissonance.game.sprites;

import com.dissonance.framework.game.GameService;
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
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.game.GameCache;
import com.dissonance.game.behaviors.Patrol;
import com.dissonance.game.behaviors.Search;
import com.dissonance.game.behaviors.WaypointLikePathFollow;
import com.dissonance.game.behaviors.WaypointLikeSeek;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.factory.Key;
import com.dissonance.game.w.FactoryFloorCat;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Admin extends Enemy {
    public Admin() {
        super("admin", StatType.NON_MAGIC, CombatType.HUMAN);
    }

    private boolean isHostile = true;
    @Override
    public void onLoad() {
        super.onLoad();

        setAttack(10);
        setDefense(12);
        setSpeed(10);
        setVigor(10);
        setStamina(100);
        setMaxStamina(100);
        setMarksmanship(10);
        setMaxMP(270);
        setHP(270);

        setCurrentWeapon(Weapon.getWeapon("admingun").createItem(this));
    }

    @Override
    public void onMovement(Direction direction) {
        if (BossQuest.END && !BossQuest.RAISE) {
            setAnimation("walk_right");
            super.setFrame(1);
            pauseAnimation();
            return;
        }
        if (BossQuest.RAISE) {
            if (isAnimationPaused()) {
                super.setFrame(1);
                playAnimation();
            }
            setAnimation("bringup_right");
            return;
        }
        if (isAttacking() || urunning || drunning || isDodging() || isDieing)
            return;
        if (isAnimationPaused()) {
            super.setFrame(1);
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
        if (BossQuest.END && !BossQuest.RAISE) {
            setAnimation("walk_right");
            super.setFrame(1);
            pauseAnimation();
            return;
        }
        if (BossQuest.RAISE) {
            onMovement(Direction.NONE);
            return;
        }
        if (isMoving() || isAttacking() || drunning || urunning || isDodging() || isDieing) {
            return;
        }
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void update() {
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

    private boolean isDieing = false;

    @Override
    public void onBlink() {
        super.onBlink();
        if (isInvincible()) {
            super.onDeath();

            if (!(GameService.getCurrentQuest() instanceof GameQuest))
                return;

            Key key = new Key();
            key.setLayer(getLayer());
            key.setX(getX());
            key.setY(getY());
            getWorld().loadAndAdd(key);
            key.setVisible(false);
            key.blink();
        }
    }

    @Override
    public void onDeath() {
        isDieing = true;
        setInvincible(true);
        setHostile(false);
        Direction direction1 = getFacingDirection();
        if (direction1 == Direction.UP || direction1 == Direction.DOWN || direction1 == Direction.RIGHT)
            setAnimation("die_right");
        else
            setAnimation("die_left");
        addAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                blink();
                removeAnimationFinishedListener(this);
            }
        });
        playAnimation();
    }

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean isHostile) {
        this.isHostile = isHostile;
    }

    private int movementSpeed() {
        return 8 + getSpeed();
    }


    @Override
    public void strike(CombatSprite attacker, WeaponItem with) {
        super.strike(attacker, with);

        if (target == attacker && getHP() < getMaxHP() / 3.0) {
            run = true;
        }
    }

    @Override
    protected void onAnimationFinished() {
        if (getCurrentAnimation().getName().startsWith("swipe")) {
            if (run && !isDodging() && target != null) {
                dodge(directionTowards(target).opposite());
            }
        }
        super.onAnimationFinished();
    }

    private boolean idle;
    private boolean run;
    private long lastAttack;
    private boolean looking = false;
    private long foundTime = 0L;
    private boolean dodgeAway;
    private PlayableSprite target;
    private static final long ATTACK_RATE_MS = 700;
    private static final long FOUND_YOU_MS = 500;
    private static final long SPOT_TIME = 2000;
    private static final Random random = new Random();
    private void runAI() {
        if (isDodging() || isAttacking())
            return;
        if (getCurrentWeapon() != null) {
            target = getClosestPlayer();
            if (target == null) {
                if (getBehavior() != null && getBehavior() instanceof Search) {
                    face(((Search)getBehavior()).getOrginalDirection());
                }
                if (!idle) {
                    setMovementSpeed(movementSpeed() / 4f);
                    idle = true;
                    Patrol patrol = new Patrol(this);
                    setBehavior(patrol);
                }
            } else {
                Direction towards = directionTowards(target);
                setMovementSpeed(movementSpeed() / 1.5f);
                idle = false;
                if (isAlignedWith(target)) {
                    if (towards != getFacingDirection())
                        face(towards);
                    if (looking) {
                        foundTime = System.currentTimeMillis();
                        looking = false;
                    }

                    if (distanceFrom(target) < 32 && run) {
                        dodgeAway();
                        dodgeAway = true;
                        return;
                    }

                    if (!bup) {
                        bringUpGun();
                        return;
                    }
                    if (isAttacking()) return;
                    if (System.currentTimeMillis() - lastAttack < ATTACK_RATE_MS) return;
                    if (System.currentTimeMillis() - foundTime < FOUND_YOU_MS) return;

                    lastAttack = System.currentTimeMillis();
                    getCurrentWeapon().use(new Integer(getLayer()));
                    setBehavior(null);
                } else {
                    if (bup) {
                        looking = true;
                        bringDownGun();
                    } else {
                        if (dodgeAway && !isDodging()) {
                            dodgeAway();
                            dodgeAway = false;
                            return;
                        }
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

    private Position getSeekTarget(PlayableSprite target) {
        if (distanceFrom(target) >= 170f)
            return target.getPosition();
        Direction direction1 = directionTowards(target);
        switch (direction1) {
            case UP:
            case DOWN:
                return new Position(target.getX(), getY());
            case LEFT:
            case RIGHT:
                return new Position(getX(), target.getY());
        }
        return target.getPosition();
    }

    private boolean bup;
    private boolean drunning = false;
    private boolean urunning = false;
    private void bringDownGun() {
        if (!bup || drunning)
            return;
        drunning = true;
        reverseAnimation(true);
        switch (getFacingDirection()) {
            case UP:
                setAnimation("bringup_up");
                playAnimation();
                break;
            case DOWN:
                setAnimation("bringup_down");
                playAnimation();
                break;
            case LEFT:
                setAnimation("bringup_left");
                playAnimation();
                break;
            case RIGHT:
                setAnimation("bringup_right");
                playAnimation();
                break;
        }
        addAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                bup = false;
                drunning = false;
                removeAnimationFinishedListener(this);
            }
        });
    }

    private void bringUpGun() {
        if (bup || urunning)
            return;
        urunning = true;
        reverseAnimation(false);
        switch (getFacingDirection()) {
            case UP:
                setAnimation("bringup_up");
                playAnimation();
                break;
            case DOWN:
                setAnimation("bringup_down");
                playAnimation();
                break;
            case LEFT:
                setAnimation("bringup_left");
                playAnimation();
                break;
            case RIGHT:
                setAnimation("bringup_right");
                playAnimation();
                break;
        }

        addAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                bup = true;
                urunning = false;
                removeAnimationFinishedListener(this);
            }
        });
    }

    private boolean isAlignedWith(PlayableSprite target) {
        if (distanceFrom(target) < 64f)
            return true;
        Direction direction = directionTowards(target);
        switch (direction) {
            case UP:
            case DOWN:
                float dif = Math.abs(getX() - target.getX());
                if (dif <= 8.5)
                    return true;
                break;
            case LEFT:
            case RIGHT:
                dif = Math.abs(getY() - target.getY());
                if (dif <= 8.5)
                    return true;
                break;
        }
        return false;
    }

    private PlayableSprite getClosestPlayer() {
        float distance = 45f * 16f;

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
}
