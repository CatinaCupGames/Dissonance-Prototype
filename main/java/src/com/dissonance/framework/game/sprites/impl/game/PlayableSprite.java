package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private PlayableSpriteEvent.OnSelectedEvent selectedEvent;
    private PlayableSpriteEvent.OnDeselectedEvent deselectedEvent;

    private boolean isPlaying = false;
    private boolean frozen;
    private boolean use_select, use_attack, use_dodge;
    private float dodgeX, dodgeY, dodgeStartX, dodgeStartY, totalDodgeTime;
    private long dodgeStartTime;
    private static PlayableSprite currentlyPlaying;
    private ArrayList<PlayableSprite> party = new ArrayList<PlayableSprite>();
    public boolean ignore_movement = false;
    private boolean is_dodging;

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
    public void setX(float x) {
        super.setX(x);
        if (isPlaying) {
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32));
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        if (isPlaying) {
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32));
        }
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (isPlaying) {
            checkSelect();
            checkMovement();
            checkKeys();
        }
    }

    protected float movementSpeed() {
        return 20 + (getSpeed() / 10);
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

    protected boolean w, a, s, d;
    protected void checkMovement() {
        if (is_dodging) {
            float moveX, moveY;
            if (dodgeX != 0) {
                moveX = Camera.ease(dodgeStartX, dodgeX, totalDodgeTime, ((System.currentTimeMillis() - dodgeStartTime)));
                setX(moveX);
                if (moveX == dodgeX) {
                    setAnimationFinishedListener(null);
                    setAnimationFrameListener(null);
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
                    return;
                }
            } else if (dodgeY != 0) {
                moveY = Camera.ease(dodgeStartY, dodgeY, totalDodgeTime, ((System.currentTimeMillis() - dodgeStartTime)));
                setY(moveY);
                if (moveY == dodgeY) {
                    setAnimationFinishedListener(null);
                    setAnimationFrameListener(null);
                    unfreeze();
                    setAnimation(0);
                    ignore_movement = false;
                    is_dodging = false;
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
                setFacing(Direction.UP);
            }
            if (s) {
                setY(getY() + (speed * RenderService.TIME_DELTA));
                setFacing(Direction.DOWN);
            }
            if (a) {
                setX(getX() - (speed * RenderService.TIME_DELTA));
                setFacing(Direction.LEFT);
            }
            if (d) {
                setX(getX() + (speed * RenderService.TIME_DELTA));
                setFacing(Direction.RIGHT);
            }
        }
    }

    protected void checkKeys() {
        if (!use_attack && !is_dodging) {
            if (InputKeys.isButtonPressed(InputKeys.ATTACK) && getCurrentWeapon() != null) {
                getCurrentWeapon().use("swipe");
                ignore_movement = true;
                use_attack = true;
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.ATTACK)) use_attack = false;
        if (!use_dodge && !is_dodging) {
            if (InputKeys.isButtonPressed(InputKeys.DODGE)) {
                frozen = true;
                Direction direction1 = getDirection();
                switch (direction1) {
                    case UP:
                        dodgeY = getY() - 100;
                        dodgeX = 0;
                        break;
                    case DOWN:
                        dodgeY = getY() + 100;
                        dodgeX = 0;
                        break;
                    case LEFT:
                        dodgeX = getX() - 100;
                        dodgeY = 0;
                        break;
                    case RIGHT:
                        dodgeX = getX() + 100;
                        dodgeY = 0;
                        break;
                    default:
                        return;
                }
                setAnimation("dodge"); //TODO Set for multiple directions
                totalDodgeTime = getAnimationSpeed() * (getFrameCount() - 1);
                totalDodgeTime -= (getSpeed() * 10);
                float timePerFrame = totalDodgeTime / (getFrameCount() - 1);
                setAnimationSpeed((int) timePerFrame);
                dodgeStartTime = System.currentTimeMillis();
                dodgeStartX = getX();
                dodgeStartY = getY();
                is_dodging = true;
                setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                    @Override
                    public void onAnimationFinished(AnimatedSprite sprite) {
                        setAnimationFinishedListener(null);
                        setAnimationFrameListener(null);
                        unfreeze();
                        setAnimation(0);
                        ignore_movement = false;
                        is_dodging = false;
                    }
                });
                ignore_movement = true;
                playAnimation();
                use_dodge = true;
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.DODGE)) use_dodge = false;
    }

    protected void checkSelect() { //TODO Make work for joypad
        if (!use_select) {
            use_select = Keyboard.isKeyDown(InputKeys.getAttackKey());

            if (use_select) {
                onSelectAttackKey();
            }

        } else if (!Keyboard.isKeyDown(InputKeys.getAttackKey())) {
            use_select = false;
        }
    }

    protected void onSelectAttackKey() {
        //TODO Detect whether to attack or select something...
        //TODO Maybe have a button to ready and unready weapon..?

        Iterator<UpdatableDrawable> sprites = getWorld().getUpdatables(); //Sprites will always be Updatable
        while (sprites.hasNext()) {
            UpdatableDrawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof Sprite) {
                Sprite sprite = (Sprite)d;
                final Vector2f v2 = sprite.getVector();
                final Vector2f v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (!isFacing(sprite, distance))
                    continue;
                System.out.println(distance);
                if (distance <= 30) {
                    sprite.onSelected(this);
                    break;
                }
            }
        }
    }

    protected boolean isFacing(Sprite s, double distance) {
        float ydif = s.getY() - getY();
        float xdif = s.getX() - getX();
        double angle = Math.toDegrees(Math.atan2(-ydif, xdif));
        while (angle < 0)
            angle += 360;
        while (angle > 360)
            angle -= 360;
        Direction direction1 = getDirection();
        if (angle != 0 && angle != -0) {
            if ((angle > 315 || angle < 45) && direction1 == Direction.RIGHT) {
                return Math.abs(xdif) < distance;
            } else if (angle > 255 && angle <= 315 && direction1 == Direction.DOWN) {
                return Math.abs(ydif) < distance;
            } else if (angle > 135 && angle <= 225 && direction1 == Direction.LEFT) {
                return Math.abs(xdif) < distance;
            } else if (angle >= 45 && angle <= 135 && direction1 == Direction.UP) {
                return Math.abs(ydif) < distance;
            }
        }
        return false;
    }


    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        frozen = true;
    }

    public void unfreeze() {
        frozen = false;
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
        use_select = false;
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

    private final Camera.CameraEaseListener listener = new Camera.CameraEaseListener() {
        @Override
        public void onEase(float x, float y, long time) {
        }

        @Override
        public void onEaseFinished() {
            isPlaying = true;
            Camera.setCameraEaseListener(null); //Reset listener
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
