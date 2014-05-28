package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.ai.behaviors.Flee;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.AbstractWaypointSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.GameCache;
import com.dissonance.game.behaviors.Patrol;
import com.dissonance.game.behaviors.Search;
import com.dissonance.game.behaviors.WaypointLikeSeek;
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

        setDefense(4);
        setSpeed(10);
        setVigor(6);
        setStamina(8);
        setMarksmanship(10);
        levelUp();

        setCurrentWeapon(Weapon.getWeapon("Revolver").createItem(this));
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

    public boolean isHostile() {
        return isHostile;
    }

    public void setHostile(boolean isHostile) {
        this.isHostile = isHostile;
    }

    private int movementSpeed() {
        return 5 + getSpeed();
    }


    private boolean run;
    private boolean idle;
    private long lastAttack;
    private boolean looking = false;
    private boolean dodgeAway = false;
    private boolean saw = false;
    private long foundTime = 0L;
    private PlayableSprite target;
    private static final long ATTACK_RATE_MS = 700;
    private static final long FOUND_YOU_MS = 200;
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
                    Patrol patrol = new Patrol(this);
                    setBehavior(patrol);
                }
            } else {
                face(directionTowards(target));

                setMovementSpeed(movementSpeed() / 1.5f);
                idle = false;
                if (isAlignedWith(target)) {
                    if (looking) {
                        foundTime = System.currentTimeMillis();
                        looking = false;
                    }

                    if (!bup) {
                        bringUpGun();
                        return;
                    }
                    if (isAttacking()) return;
                    if (System.currentTimeMillis() - lastAttack < ATTACK_RATE_MS) return;
                    if (System.currentTimeMillis() - foundTime < FOUND_YOU_MS) return;

                    lastAttack = System.currentTimeMillis();
                    getCurrentWeapon().use(new Integer(target.getLayer()));
                    setBehavior(null);
                } else {
                    if (bup) {
                        looking = true;
                        bringDownGun();
                    } else {
                        Direction direction1 = directionTowards(target);
                        switch (direction1) {
                            case UP:
                            case DOWN:
                                float dif = getX() - target.getX();
                                if (dif > 6.5)
                                    setX(getX() - (movementSpeed() * RenderService.TIME_DELTA));
                                else if (dif < 6.5)
                                    setX(getX() + (movementSpeed() * RenderService.TIME_DELTA));
                                break;
                            case LEFT:
                            case RIGHT:
                                dif = getY() - target.getY();
                                if (dif > 6.5)
                                    setY(getY() - (movementSpeed() * RenderService.TIME_DELTA));
                                else if (dif < 6.5)
                                    setY(getY() + (movementSpeed() * RenderService.TIME_DELTA));
                                break;
                        }
                    }
                }
            }
        }
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
        setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                bup = false;
                drunning = false;
                setAnimationFinishedListener(null);
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

        setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                bup = true;
                urunning = false;
                setAnimationFinishedListener(null);
            }
        });
    }

    private boolean isAlignedWith(PlayableSprite target) {
        Direction direction = directionTowards(target);
        switch (direction) {
            case UP:
            case DOWN:
                float dif = Math.abs(getX() - target.getX());
                System.out.println(dif);
                if (dif <= 8.5)
                    return true;
                break;
            case LEFT:
            case RIGHT:
                dif = Math.abs(getY() - target.getY());
                System.out.println(dif);
                if (dif <= 8.5)
                    return true;
                break;
        }
        return false;
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
