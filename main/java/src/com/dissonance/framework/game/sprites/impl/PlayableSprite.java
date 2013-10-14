package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private PlayableSpriteEvent.OnSelectedEvent selectedEvent;
    private PlayableSpriteEvent.OnDeselectedEvent deselectedEvent;

    private boolean isPlaying = false;
    private boolean frozen;
    private boolean attack_select;
    private static PlayableSprite currentlyPlaying;
    private ArrayList<PlayableSprite> party = new ArrayList<PlayableSprite>();

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
        if (isPlaying) {
            checkSelect();
            checkMovement();
        }
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
        if (frozen)
            return;
        if (InputKeys.usingController()) {
            float xspeed = InputKeys.getJoypadValue(InputKeys.MOVEX) * (10 * RenderService.TIME_DELTA);
            float yspeed = InputKeys.getJoypadValue(InputKeys.MOVEY) * (10 * RenderService.TIME_DELTA);

            setX(getX() + xspeed);
            setY(getY() + yspeed);
        } else {
            w = InputKeys.isButtonPressed(InputKeys.MOVEUP);
            d = InputKeys.isButtonPressed(InputKeys.MOVERIGHT);
            s = InputKeys.isButtonPressed(InputKeys.MOVEDOWN);
            a = InputKeys.isButtonPressed(InputKeys.MOVELEFT);

            if (w) {
                setY(getY() - (10 * RenderService.TIME_DELTA));
                setFacing(Direction.UP);
            }
            if (s) {
                setY(getY() + (10 * RenderService.TIME_DELTA));
                setFacing(Direction.DOWN);
            }
            if (a) {
                setX(getX() - (10 * RenderService.TIME_DELTA));
                setFacing(Direction.LEFT);
            }
            if (d) {
                setX(getX() + (10 * RenderService.TIME_DELTA));
                setFacing(Direction.RIGHT);
            }
        }
    }

    protected void checkSelect() {
        if (!attack_select) {
            attack_select = Keyboard.isKeyDown(InputKeys.getAttackKey());

            if (attack_select) {
                onSelectAttackKey();
            }

        } else if (!Keyboard.isKeyDown(InputKeys.getAttackKey())) {
            attack_select = false;
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
                final Vec2 v2 = sprite.getVector();
                final Vec2 v1 = getVector();
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
        int xadd = 0;
        int yadd = 0;
        boolean xcheck = getDirection() == Direction.UP || getDirection() == Direction.DOWN;
        if (getDirection() == Direction.UP)
            yadd = -1;
        else if (getDirection() == Direction.DOWN)
            yadd = 1;
        else if (getDirection() == Direction.LEFT)
            xadd = -1;
        else if (getDirection() == Direction.RIGHT)
            xadd = 1;

        final Vec2 v2 = s.getVector();
        final Vec2 v1 = getVector();
        double new_distance = Math.sqrt(((v2.x - (v1.x + xadd)) * (v2.x - (v1.x + xadd))) + ((v2.y - (v1.y + yadd)) * (v2.y - (v1.y + yadd))));

        return new_distance < distance;
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
     * If the player is currently playing as another Sprite, then the {@link com.dissonance.framework.game.sprites.impl.PlayableSprite#onDeselect()} will be
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
        attack_select = false;
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
