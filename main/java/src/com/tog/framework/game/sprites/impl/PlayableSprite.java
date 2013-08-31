package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.input.InputKeys;
import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.Camera;
import com.tog.framework.render.Drawable;
import com.tog.framework.render.RenderService;
import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;

import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private boolean isPlaying = false;
    private boolean frozen = false;
    private boolean attack_select;
    private static PlayableSprite currentlyPlaying;


    @Override
    public void setX(float x) {
        super.setX(x);
        if (isPlaying) {
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
        }
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        if (isPlaying){
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
        }
    }

    @Override
    public void update() {
        if (isPlaying) {
            checkSelect();
            checkMovement();
        }
    }

    protected boolean w, a, s, d;
    protected void checkMovement() {
        w = Keyboard.isKeyDown(InputKeys.getMoveUpKey());
        d = Keyboard.isKeyDown(InputKeys.getMoveRightKey());
        s = Keyboard.isKeyDown(InputKeys.getMoveDownKey());
        a = Keyboard.isKeyDown(InputKeys.getMoveLeftKey());

        if (w)
            setY(getY() - (10 * RenderService.TIME_DELTA));
        if (s)
            setY(getY() + (10 * RenderService.TIME_DELTA));
        if (a)
            setX(getX() - (10 * RenderService.TIME_DELTA));
        if (d)
            setX(getX() + (10 * RenderService.TIME_DELTA));
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

        Iterator<Drawable> sprites = getWorld().getDrawable();
        while (sprites.hasNext()) {
            Drawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof Sprite) {
                Sprite sprite = (Sprite)d;
                final Vec2 v2 = sprite.getVector();
                final Vec2 v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (distance <= 1) {
                    sprite.onSelected(this);
                    break;
                }
            }
        }
    }


    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Select this sprite to be the sprite the player will play as <br></br>
     * If the player is currently playing as another Sprite, then the {@link com.tog.framework.game.sprites.impl.PlayableSprite#onDeselect()} will be
     * invoke on that sprite. <br></br>
     *
     * The Camera will pan to the newly selected sprite
     */
    public void select() {
        if (currentlyPlaying != null) {
            currentlyPlaying.deselect();
        }

        currentlyPlaying = this;

        Camera.setCameraEaseListener(listener);
        Camera.easeMovement(Camera.translateToCameraCenter(getVector(), 32, 32), 800);
    }

    public void deselect() {
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
}
