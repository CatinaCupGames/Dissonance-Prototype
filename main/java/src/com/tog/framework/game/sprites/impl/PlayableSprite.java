package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.Camera;
import com.tog.framework.render.Drawable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private boolean isPlaying = false;
    private boolean frozen = false;
    private static PlayableSprite currentlyPlaying;

    @Override
    public void setX(float x) {
        super.setX(x);
        if (isPlaying)
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        if (isPlaying)
            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32, 32));
    }

    boolean attack_select;
    @Override
    public void update() {

    }

    protected void checkSelect() {
        if (!attack_select) {
            attack_select = Keyboard.isKeyDown(Keyboard.KEY_J);

            if (attack_select) {
                onSelectAttackKey();
            }

        } else if (!Keyboard.isKeyDown(Keyboard.KEY_J)) {
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
                final Vector2f v2 = sprite.getVector();
                final Vector2f v1 = getVector();
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
     * If the player is currently playing as another Sprite, then the {@link com.tog.framework.game.sprites.impl.PlayableSprite#deselect()} will be
     * invoke on that sprite. <br></br>
     *
     * The Camera will pan to the newly selected sprite
     */
    public void select() {
        if (currentlyPlaying != null)
            currentlyPlaying.deselect();

        currentlyPlaying = this;

        Camera.setCameraEaseListener(listener);
        Camera.easeMovement(Camera.translateToCameraCenter(getVector(), 32, 32), 800);
    }

    public void deselect() {
        isPlaying = false;
        currentlyPlaying = null;
        Camera.setCameraEaseListener(null); //Safety net
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
