package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.Timer;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private PlayableSpriteEvent.OnSelectedEvent selectedEvent;
    private PlayableSpriteEvent.OnDeselectedEvent deselectedEvent;

    private boolean isPlaying = false;
    private boolean usespell1, usespell2;
    private boolean frozen;
    private boolean use_attack;
    private boolean use_dodge;
    private float dodgeX, dodgeY, dodgeStartX, dodgeStartY, totalDodgeTime;
    private long dodgeStartTime;
    private static PlayableSprite currentlyPlaying;
    private ArrayList<PlayableSprite> party = new ArrayList<PlayableSprite>();
    public boolean ignore_movement = false;
    private boolean is_dodging, allow_dodge = true;

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

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return sprite instanceof PlayableSprite && party.contains(sprite);
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (isPlaying) {
            checkMovement();
            checkKeys();
        }
    }

    protected float movementSpeed() {
        return 10 + (getSpeed() / 10);
    }

    public void joinParty(PlayableSprite joiner) {
        for (PlayableSprite p : party) {
            if (!p.party.contains(joiner))
                p.party.add(joiner); //Add the newcomer to everyone elses party
            if (!joiner.party.contains(p))
                joiner.party.add(p); //Add everyone else to the newcomer's party
        }
        if (!party.contains(joiner))
            party.add(joiner); //Add the newcomer to this players party
        if (!joiner.party.contains(this))
            joiner.party.add(this); //Add this player to the newcomer's party
    }

    public PlayableSprite[] getParty() {
        return party.toArray(new PlayableSprite[party.size()]);
    }

    private boolean w, a, s, d;
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
                setX(moveX);
                if (moveX == dodgeX) {
                    setAnimationFinishedListener(null);
                    setAnimationFrameListener(null);
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
                    Timer.delayedInvokeRunnable(500, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
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
                setY(moveY);
                if (moveY == dodgeY) {
                    setAnimationFinishedListener(null);
                    setAnimationFrameListener(null);
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
                    Timer.delayedInvokeRunnable(500, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
                        }
                    });
                    return;
                }
            }
        }
        if (frozen)
            return;
        float speed = movementSpeed();
        if (InputKeys.usingController()) {
            Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.MOVEX), InputKeys.getJoypadValue(InputKeys.MOVEY));
            if (values.lengthSquared() < 0.25f)
                values = new Vector2f(0,0);

            setX(getX() + values.x * (speed * RenderService.TIME_DELTA));
            setY(getY() + values.y * (speed * RenderService.TIME_DELTA));
            double angle = Math.toDegrees(Math.atan2(-values.y, values.x));
            if (angle < 0)
                angle += 360;
            if (angle != 0 && angle != -0) {
                if (angle > 315 || angle < 45) {
                    setFacing(Direction.RIGHT);
                } else if (angle > 255 && angle <= 315) {
                    setFacing(Direction.DOWN);
                } else if (angle > 135 && angle <= 225) {
                    setFacing(Direction.LEFT);
                } else if (angle >= 45 && angle <= 135) {
                    setFacing(Direction.UP);
                }
            }
        } else {
            w = InputKeys.isButtonPressed(InputKeys.MOVEUP);
            d = InputKeys.isButtonPressed(InputKeys.MOVERIGHT);
            s = InputKeys.isButtonPressed(InputKeys.MOVEDOWN);
            a = InputKeys.isButtonPressed(InputKeys.MOVELEFT);

            if (w) {
                setY(getY() - (speed * RenderService.TIME_DELTA));
                setFacing(a ? Direction.UP_LEFT : d ? Direction.UP_RIGHT : Direction.UP);
            }
            if (s) {
                setY(getY() + (speed * RenderService.TIME_DELTA));
                setFacing(a ? Direction.DOWN_LEFT : d ? Direction.DOWN_RIGHT : Direction.DOWN);
            }
            if (a) {
                setX(getX() - (speed * RenderService.TIME_DELTA));
                setFacing(w ? Direction.UP_LEFT : s ? Direction.DOWN_LEFT : Direction.LEFT);
            }
            if (d) {
                setX(getX() + (speed * RenderService.TIME_DELTA));
                setFacing(w ? Direction.UP_RIGHT : s ? Direction.DOWN_RIGHT : Direction.RIGHT);
            }

            if (!ignore_movement) {
                if (!w && !d && !s && !a)
                    onNoMovement();
                else
                    onMovement(getDirection().simple());
            }
        }
    }

    protected void checkKeys() {
        if (!use_attack && !is_dodging) {
            if (InputKeys.isButtonPressed(InputKeys.ATTACK)) {
                boolean skip = checkSelect();
                if (!skip && getCurrentWeapon() != null) {
                    getCurrentWeapon().use("swipe");
                    ignore_movement = true;
                }
                use_attack = true;
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.ATTACK)) use_attack = false;

        if (!use_dodge && !is_dodging && allow_dodge) {
            if (InputKeys.isButtonPressed(InputKeys.DODGE)) {
                dodge(getDirection());
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.DODGE)) use_dodge = false;

        if (!usespell1) {
            if (InputKeys.isButtonPressed(InputKeys.MAGIC1)) {
                if (hasSpell1())
                    useSpell1();
                else {
                    //TODO Play sound
                }
                usespell1 = true;
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.MAGIC1)) usespell1 = false;

        if (!usespell2) {
            if (InputKeys.isButtonPressed(InputKeys.MAGIC2)) {
                if (hasSpell2())
                    useSpell2();
                else {
                    //TODO Play sound
                }
                usespell2 = true;
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.MAGIC2)) usespell2 = false;
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
        Direction direction1 = getDirection();
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

    public void freeze() {
        freeze(false, null);
    }

    private ArrayList<Class<?>> classLockers = new ArrayList<>();
    /**
     * Freeze this sprite and <b>prevent it from receiving input from a controller.</b> <br></br>
     * The <b>lock</b> parameter specifies whether to lock the call to {@link com.dissonance.framework.game.sprites.impl.game.PlayableSprite#unfreeze(Class)} so only the class <b>classLocker</b> can
     * unfreeze the player. <br></br>
     * If another class already has a lock on the freeze, then a "list of locks" is created. In order for the player to unfreeze, all
     * classes that requested the freeze must invoke {@link com.dissonance.framework.game.sprites.impl.game.PlayableSprite#unfreeze(Class)}.
     * If the same class invokes this method with a lock request 2 times, then the same class must invoke {@link PlayableSprite#unfreeze()} 2 times respectively. <br></br>
     * This is behavior is intended for cases such as where
     * one menu class has a lock on the freeze, but then another menu appears and steals the lock. When that menu closes and the old menu remains open, the player will
     * be unfrozen. This is also to prevent one menu class from opening up another menu where that class cannot receive a lock, and when it closes both menus, the player is stuck frozen. <br></br>
     * To unlock the freeze, you <b>MUST</b> call {@link com.dissonance.framework.game.sprites.impl.game.PlayableSprite#unfreeze(Class)} passing the locker class as a parameter.
     * @param lock Whether or not to request a lock to the call to {@link PlayableSprite#unfreeze()}
     * @param classLocker The class that can unlock (unfreeze) the player. This is normally the calling class
     */
    public void freeze(boolean lock, Class<?> classLocker) {
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
        if (classLockers.size() == 0) frozen = false;
        return classLockers.size() == 0;
    }

    /**
     * Select this sprite to be the sprite the player will play as <br></br>
     * If the player is currently playing as another Sprite, then the {@link PlayableSprite#onDeselect()} will be
     * invoke on that sprite. <br></br>
     *
     * The Camera will pan to the newly selected sprite
     */
    public void select() {
        if (selectedEvent != null) {
            selectedEvent.onSelectedEvent(this);
        }

        if (currentlyPlaying != null) {
            currentlyPlaying.deselect();
        }

        currentlyPlaying = this;

        Camera.setCameraEaseListener(listener);
        Camera.easeMovement(Camera.translateToCameraCenter(getVector(), 32), 800);
    }

    public void deselect() {
        if (deselectedEvent != null) {
            deselectedEvent.onDeselectedEvent(this);
        }
        Camera.followSprite(null);
        onDeselect();
        if (currentlyPlaying != null)
            throw new RuntimeException("super.onDeselect was not executed! Try putting super.onDeselect at the top of your method!");
    }

    protected void onDeselect() {
        isPlaying = false;
        currentlyPlaying = null;
        Camera.setCameraEaseListener(null); //Safety net

        w = false;
        a = false;
        s = false;
        d = false;
    }


    protected void dodge(Direction direction1) {
        frozen = true;
        switch (direction1) {
            case UP:
                int i = 0;
                for (; i < 80; i++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() - i))
                        break;
                }
                dodgeY = getY() - i;
                dodgeX = 0;
                break;
            case DOWN:
                int ii = 0;
                for (; ii < 80; ii++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() + ii))
                        break;
                }
                dodgeY = getY() + ii;
                dodgeX = 0;
                break;
            case LEFT:
                int iii = 0;
                for (; iii < 80; iii++) {
                    if (getHitBox().checkForCollision(this, getX() - iii, getY()))
                        break;
                }
                dodgeX = getX() - iii;
                dodgeY = 0;
                break;
            case RIGHT:
                int iiii = 0;
                for (; iiii < 80; iiii++) {
                    if (getHitBox().checkForCollision(this, getX() + iiii, getY()))
                        break;
                }
                dodgeX = getX() + iiii;
                dodgeY = 0;
                break;
            default:
                return;
        }
        setAnimation("dodge"); //TODO Set for multiple directions
        totalDodgeTime = getAnimationSpeed() * (getFrameCount() - 1);
        totalDodgeTime -= (getSpeed() + 20) * 15;
        if (totalDodgeTime < 350)
            totalDodgeTime = 350;
        float timePerFrame = totalDodgeTime / (getFrameCount() - 1);
        setAnimationSpeed((int) timePerFrame);
        dodgeStartTime = System.currentTimeMillis();
        dodgeStartX = getX();
        dodgeStartY = getY();
        is_dodging = true;
        allow_dodge = false;
        setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                setAnimationFinishedListener(null);
                setAnimationFrameListener(null);
                unfreeze();
                setAnimation(0);
                ignore_movement = false;
                is_dodging = false;
                Timer.delayedInvokeRunnable(500, new Runnable() {
                    @Override
                    public void run() {
                        allow_dodge = true;
                    }
                });
            }
        });
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

    public static PlayableSprite getCurrentlyPlayingSprite() {
        return currentlyPlaying;
    }

    public static void haltMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.frozen = true;
    }

    public static void resumeMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.frozen = false;
    }

    private final Camera.CameraMovementListener listener = new Camera.CameraMovementListener() {
        @Override
        public void onMovement(float x, float y, long time) {
        }

        @Override
        public void onMovementFinished() {
            isPlaying = true;
            Camera.setCameraEaseListener(null); //Reset listener
            Camera.followSprite(PlayableSprite.this);
        }
    };

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
