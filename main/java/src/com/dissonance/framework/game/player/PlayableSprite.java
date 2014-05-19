package com.dissonance.framework.game.player;

import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.framework.system.utils.Timer;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private PlayableSpriteEvent.OnSelectedEvent selectedEvent;
    private PlayableSpriteEvent.OnDeselectedEvent deselectedEvent;
    private Input input;

    private boolean isPlaying = false;
    private MovementType mType = MovementType.RUNNING;
    private boolean frozen;


    boolean controller_extend;
    boolean keyboard_extend;
    boolean use_dodge;
    boolean isAttacking = false;

    float dodgeX, dodgeY, dodgeStartX, dodgeStartY, totalDodgeTime;
    long dodgeStartTime;
    boolean is_dodging, allow_dodge = true;
    ArrayList<PlayableSprite> party = new ArrayList<PlayableSprite>();

    private static PlayableSprite currentlyPlaying;
    private CombatSprite locked;
    public boolean ignore_movement = false;

    private boolean appear;
    private long startTime;
    private float start;
    private float end;
    private Runnable appearRunnable;
    private ParticleSprite.ParticleSource source;

    public PlayableSprite(Input input) {
        super();
        this.input = input;
    }

    public PlayableSprite() {
        super();
    }

    /**
     * Returns whether this sprite is playable or not. A sprite is playable when they have an {@link com.dissonance.framework.game.player.Input}
     * attach to them.
     * @return Whether this sprite is playable or not.
     */
    public boolean isPlayable() {
        return input != null;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Input getInput() {
        return input;
    }

    public void appear() {
        appear(null);
    }

    public void appear(Runnable runnable) {
        appear = true;

        startTime = System.currentTimeMillis();
        start = getAlpha();
        end = 1f;

        if (source != null)
            source.end();

        source = ParticleSprite.createParticlesAt(getX(), (getY() + getHeight() / 2f), getWorld())
                .setCount(50)
                .setRate(300)
                .setTime(800)
                .setSpeed(4);

        appearRunnable = runnable;
    }

    public void disappear() {
        disappear(null);
    }

    public void disappear(Runnable runnable) {
        appear = true;

        startTime = System.currentTimeMillis();
        start = getAlpha();
        end = 0f;

        if (source != null)
            source.end();

        source = ParticleSprite.createParticlesAt(getX(), (getY() + getHeight() / 2f), getWorld())
                .setCount(50)
                .setRate(150)
                .setTime(800)
                .setSpeed(4);

        appearRunnable = runnable;
    }

    /**
     * Sets this {@link PlayableSprite PlayableSprite's}
     * {@link PlayableSpriteEvent.OnSelectedEvent OnSelectedEvent listener} to the specified listener.
     *
     * @param selectedListener The new event listener.
     */
    public void setSelectedListener(PlayableSpriteEvent.OnSelectedEvent selectedListener) {
        this.selectedEvent = selectedListener;
    }

    /**
     * Sets this {@link PlayableSprite PlayalbeSprite's}
     * {@link PlayableSpriteEvent.OnDeselectedEvent OnDeselectedEvent listener} to the specified listener.
     *
     * @param deselectedListener The new event listener.
     */
    public void setDeselectedListener(PlayableSpriteEvent.OnDeselectedEvent deselectedListener) {
        this.deselectedEvent = deselectedListener;
    }

    public MovementType getMovementType() {
        return mType;
    }

    public void setMovementType(MovementType type) {
        if (type == MovementType.FROZEN) {
            oMType = this.mType;
        }
        this.mType = type;
        if (mType == MovementType.FROZEN)
            freeze();
    }

    public boolean isPlayer1() {
        return player != null && player.getNumber() == 1;
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return sprite instanceof PlayableSprite && party.contains(sprite);
    }

    @Override
    public boolean isMoving() {
        return input != null && input.isMoving(this);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean value) {
        this.isAttacking = value;
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (isPlaying) {
            input.update();

            checkMovement();
            input.checkKeys(this);
        }

        if (appear) {
            float value = Camera.ease(start, end, 800, (System.currentTimeMillis() - startTime));
            setAlpha(value);
            if (value == end) {
                appear = false;
                source.end();

                source = null;

                if (appearRunnable != null)
                    appearRunnable.run();

                if (end == 0f)
                    setVisible(false);
                else
                    setVisible(true);
            }
        }
    }

    protected float movementSpeed() {
        switch (mType) {
            case WALKING:
                return 10 + (getSpeed() / 2);
            case RUNNING:
                return 15 + (getSpeed());
            default:
                return 0;
        }
    }

    public void joinParty(PlayableSprite joiner) {
        for (PlayableSprite p : party) {
            if (!p.party.contains(joiner)) {
                p.party.add(joiner); //Add the newcomer to everyone elses party
                p.ignoreCollisionWith(joiner);
            }
            if (!joiner.party.contains(p)) {
                joiner.party.add(p); //Add everyone else to the newcomer's party
                joiner.ignoreCollisionWith(p);
            }
        }
        if (!party.contains(joiner)) {
            party.add(joiner); //Add the newcomer to this players party
            ignoreCollisionWith(joiner);
        }
        if (!joiner.party.contains(this)) {
            joiner.party.add(this); //Add this player to the newcomer's party
            joiner.ignoreCollisionWith(this);
        }
    }

    public PlayableSprite[] getParty() {
        return party.toArray(new PlayableSprite[party.size()]);
    }

    boolean w, a, s, d;
    protected void checkMovement() {
        if (is_dodging) {
            float moveX, moveY;
            if (dodgeX != 0) {
                float dif = (float)(System.currentTimeMillis() - dodgeStartTime);
                float percent;
                if (dif > totalDodgeTime) {
                    percent = 1;
                } else {
                    percent = dif / totalDodgeTime;
                }
                moveX = dodgeStartX + ((dodgeX - dodgeStartX) * percent);
                rawSetX(moveX);
                if (moveX == dodgeX) {
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
                    Timer.delayedInvokeRunnable(100, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
                            if (isFrozen())
                                unfreeze();
                        }
                    });
                    return;
                }
            } else if (dodgeY != 0) {
                float dif = (float)(System.currentTimeMillis() - dodgeStartTime);
                float percent;
                if (dif > totalDodgeTime) {
                    percent = 1;
                } else {
                    percent = dif / totalDodgeTime;
                }
                moveY = dodgeStartY + ((dodgeY - dodgeStartY) * percent);
                //moveY = Camera.ease(dodgeStartY, dodgeY, totalDodgeTime, ((System.currentTimeMillis() - dodgeStartTime)));
                rawSetY(moveY);
                if (moveY == dodgeY) {
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
                    Timer.delayedInvokeRunnable(100, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
                            if (isFrozen())
                                unfreeze();
                        }
                    });
                    return;
                }
            }
        }
        if (frozen)
            return;
        if (ignore_movement && !isAttacking() && !is_dodging)
            ignore_movement = false;

        input.checkMovement(this);
    }

    void _onNoMovement() { onNoMovement(); }

    void _onMovement(Direction direction) { onMovement(direction); }

    public void findLock() {
        double lowest = 0;
        CombatSprite lowestC = null;
        Iterator<UpdatableDrawable> sprites = getWorld().getUpdatables();
        while (sprites.hasNext()) {
            UpdatableDrawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof CombatSprite) {
                CombatSprite combatSprite = (CombatSprite)d;

                if (isAlly(combatSprite))
                    continue;

                final Vector2f v2 = combatSprite.getVector();
                final Vector2f v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (distance < 150) {
                    if (distance < lowest || lowestC == null) {
                        lowestC = combatSprite;
                        lowest = distance;
                    }
                }
            }
        }

        locked = lowestC;
        if (locked != null) {
            locked.lockOn(this);
        }
    }

    public void clearLock() {
        if (locked != null) {
            locked.removeLock(this);
        }
        locked = null;
    }

    protected boolean checkSelect() {
        Iterator<UpdatableDrawable> sprites = getWorld().getUpdatables(); //Sprites will always be Updatable
        while (sprites.hasNext()) {
            UpdatableDrawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof Selectable) {
                Selectable sprite = (Selectable)d;
                final Vector2f v2 = sprite.getVector();
                final Vector2f v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (!isFacing(sprite, distance))
                    continue;
                if (distance <= 50) {
                    sprite.onSelected(this);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isFacing(Selectable s, double distance) {
        float ydif = s.getY() - getY();
        float xdif = s.getX() - getX();
        double angle = Math.toDegrees(Math.atan2(-ydif, xdif));
        while (angle < 0)
            angle += 360;
        while (angle > 360)
            angle -= 360;
        Direction direction1 = getFacingDirection();
        if ((angle > 315 || angle < 45) && direction1 == Direction.RIGHT) {
            return Math.abs(xdif) < distance;
        } else if (angle > 255 && angle <= 315 && direction1 == Direction.DOWN) {
            return Math.abs(ydif) < distance;
        } else if (angle > 135 && angle <= 225 && direction1 == Direction.LEFT) {
            return Math.abs(xdif) < distance;
        } else if (angle >= 45 && angle <= 135 && direction1 == Direction.UP) {
            return Math.abs(ydif) < distance;
        }
        return false;
    }


    public boolean isFrozen() {
        return frozen;
    }

    private MovementType oMType;
    public void freeze() {
        freeze(false, null);
    }

    private ArrayList<Class<?>> classLockers = new ArrayList<>();

    /**
     * Freeze this sprite and <b>prevent it from receiving input from a controller.</b> <br></br>
     * The <b>lock</b> parameter specifies whether to lock the call to {@link PlayableSprite#unfreeze(Class)} so only the class <b>classLocker</b> can
     * unfreeze the player. <br></br>
     * If another class already has a lock on the freeze, then a "list of locks" is created. In order for the player to unfreeze, all
     * classes that requested the freeze must invoke {@link PlayableSprite#unfreeze(Class)}.
     * If the same class invokes this method with a lock request 2 times, then the same class must invoke {@link PlayableSprite#unfreeze()} 2 times respectively. <br></br>
     * This is behavior is intended for cases such as where
     * one menu class has a lock on the freeze, but then another menu appears and steals the lock. When that menu closes and the old menu remains open, the player will
     * be unfrozen. This is also to prevent one menu class from opening up another menu where that class cannot receive a lock, and when it closes both menus, the player is stuck frozen. <br></br>
     * To unlock the freeze, you <b>MUST</b> call {@link PlayableSprite#unfreeze(Class)} passing the locker class as a parameter.
     * @param lock Whether or not to request a lock to the call to {@link PlayableSprite#unfreeze()}
     * @param classLocker The class that can unlock (unfreeze) the player. This is normally the calling class
     */
    public void freeze(boolean lock, Class<?> classLocker) {
        if (mType != MovementType.FROZEN) {
            this.oMType = this.mType;
            this.mType = MovementType.FROZEN;
        }
        frozen = true;
        onNoMovement();
        if (lock) {
            if (classLocker == null)
                throw new InvalidParameterException("The class locker cannot be null when requesting a lock!");
            classLockers.add(classLocker);
        }
    }

    public boolean unfreeze() {
        return unfreeze(null);
    }

    public boolean unfreeze(Class<?> classLocker) {
        if (classLocker != null) {
            if (classLockers.contains(classLocker))
                classLockers.remove(classLocker);
        }
        if (classLockers.size() == 0) {
            if (this.mType == MovementType.FROZEN) {
                this.mType = this.oMType;
            }
            frozen = false;
        }
        return classLockers.size() == 0;
    }

    private Player player;

    void select(Player player) {
        if (!isPlayable())
            throw new InvalidParameterException("This sprite has no input controller!");

        setBehavior(null);

        if (selectedEvent != null) {
            selectedEvent.onSelectedEvent(this);
        }

        isPlaying = true;

        Camera.followSprite(this);

        this.player = player;
    }

    @Override
    public void setBehavior(Behavior behavior) {
        if (!isPlaying())
            super.setBehavior(behavior);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * @deprecated This method is deprecated, please read the co-op wiki for more details. This method will be removed once all dependencies are resolved.
     */
    @Deprecated
    public void select() { }

    public void deselect() {
        if (deselectedEvent != null) {
            deselectedEvent.onDeselectedEvent(this);
        }
        Camera.stopFollowing(this);
        onDeselect();
        if (isPlaying)
            throw new RuntimeException("super.onDeselect was not executed! Try putting super.onDeselect at the top of your method!");
    }

    protected void onDeselect() {
        isPlaying = false;
        Camera.setCameraEaseListener(null); //Safety net

        w = false;
        a = false;
        s = false;
        d = false;
        player = null;
        input = null;
    }

    protected void dodge(Direction direction1) {
        if (frozen)
            return;
        String ani;
        float speed = movementSpeed() * 8.5f;
        freeze();
        /*

        ALGEBRA TIME!

        d = distance
        s = speed
        t = total dodge time
        f = time per frame

        t = 4f
        s = 2.5 * movementSpeed()
        d = s * (t / f)
        f = (st) / d


        ==============================

        As long as t = 4f, then d = 4s
         */
        int DISTANCE = 150; //TODO Maybe change this
        DISTANCE *= 0.8f;
        switch (direction1) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                ani = "dodge_up";
                int i = 0;
                for (; i < DISTANCE; i++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() - i))
                        break;
                }
                dodgeY = getY() - i;
                dodgeX = 0;
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                ani = "dodge_down";
                int ii = 0;
                for (; ii < DISTANCE; ii++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() + ii))
                        break;
                }
                dodgeY = getY() + ii;
                dodgeX = 0;
                break;
            case LEFT:
                ani = "dodge_left";
                int iii = 0;
                for (; iii < DISTANCE; iii++) {
                    if (getHitBox().checkForCollision(this, getX() - iii, getY()))
                        break;
                }
                dodgeX = getX() - iii;
                dodgeY = 0;
                break;
            case RIGHT:
                ani = "dodge_right";
                int iiii = 0;
                for (; iiii < DISTANCE; iiii++) {
                    if (getHitBox().checkForCollision(this, getX() + iiii, getY()))
                        break;
                }
                dodgeX = getX() + iiii;
                dodgeY = 0;
                break;
            default:
                unfreeze();
                return;
        }
        setAnimation(ani);
        setAnimationSpeed(150);
        totalDodgeTime = 4 * getAnimationSpeed();
        totalDodgeTime -= speed;
        setAnimationSpeed((int) (((1f/4f) * speed) + ((1f/4f) * totalDodgeTime)));
        dodgeStartTime = System.currentTimeMillis();
        dodgeStartX = getX();
        dodgeStartY = getY();
        is_dodging = true;
        allow_dodge = false;
        ignore_movement = true;
        playAnimation();
        use_dodge = true;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isSelected() {
        return isPlaying;
    }

    /**
     * @deprecated This method is deprecated, see the coop wiki for more info
     */
    @Deprecated
    public static PlayableSprite getCurrentlyPlayingSprite() {
        return null;
    }

    @Deprecated
    public static void haltMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.freeze();
    }

    @Deprecated
    public static void resumeMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.freeze();
    }

    private final Camera.CameraMovementListener listener = new Camera.CameraMovementListener() {
        @Override
        public void onMovement(float x, float y, long time) {
        }

        @Override
        public void onMovementFinished() {
            Camera.setCameraEaseListener(null); //Reset listener
            Camera.followSprite(PlayableSprite.this);
        }
    };

    public CombatSprite getLocker() {
        return locked;
    }



    public interface PlayableSpriteEvent {
        /**
         * Interface definition for a callback to be invoked when the {@link PlayableSprite} has been selected by the player.
         */
        public interface OnSelectedEvent {
            public void onSelectedEvent(PlayableSprite sprite);
        }

        /**
         * Interface definition for a callback to be invoked when the {@link PlayableSprite} has been deselected.
         */
        public interface OnDeselectedEvent {
            public void onDeselectedEvent(PlayableSprite sprite);
        }
    }
}
