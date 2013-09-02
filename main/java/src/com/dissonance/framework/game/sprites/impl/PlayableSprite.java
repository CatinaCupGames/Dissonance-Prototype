package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.input.InputService;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.Settings;
import com.dissonance.framework.system.utils.Direction;
import net.java.games.input.Component;
import net.java.games.input.Controller;
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
        if (isPlaying) {
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

	public static void enableController(String controller)
	{
		for(int i = 0; i < Settings.controllers.size(); i++)
		{
			if(Settings.controllers.keySet().toArray(new Controller[Settings.controllers.size()])[i].getName().equals(controller))
			{
                Settings.controllers.put(Settings.controllers.keySet().toArray(new Controller[Settings.controllers.size()])[i], true);
                Settings.enabledController = controller;
			} else {
				Settings.controllers.put(Settings.controllers.keySet().toArray(new Controller[Settings.controllers.size()])[i], false);
			}
		}
	}

    private boolean isDown(Component.Identifier component)
    {
        for(int i = 0; i < Settings.controllers.size(); i++)
        {
            if(InputService.isControllerEnabled(Settings.controllers.keySet().toArray(new Controller[Settings.controllers.size()])[i].getName()))
            {
                if(InputService.getButtonState(Settings.controllers.keySet().toArray(new Controller[Settings.controllers.size()])[i].getName(), component))
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean w, a, s, d;
    protected void checkMovement() {
        if(Settings.usingController)
        {
            // TODO: Move using D-Pad.
            // RIGHT NOW IT'S JUST USING THE BASIC SHAPES.
            w = isDown(Component.Identifier.Button._3);
            d = isDown(Component.Identifier.Button._2);
            s = isDown(Component.Identifier.Button._0);
            a = isDown(Component.Identifier.Button._1);
        } else {
            w = Keyboard.isKeyDown(InputKeys.getMoveUpKey());
            d = Keyboard.isKeyDown(InputKeys.getMoveRightKey());
            s = Keyboard.isKeyDown(InputKeys.getMoveDownKey());
            a = Keyboard.isKeyDown(InputKeys.getMoveLeftKey());
        }

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

                if (!isFacing(sprite, distance))
                    continue;
                System.out.println(distance);
                if (distance <= 25) {
                    sprite.onSelected(this);
                    break;
                }
            }
        }
    }

    protected boolean isFacing(Sprite s, double distance) {
        int xadd = 0;
        int yadd = 0;
        if (getFacingDirection() == Direction.UP)
            yadd = -1;
        else if (getFacingDirection() == Direction.DOWN)
            yadd = 1;
        else if (getFacingDirection() == Direction.LEFT)
            xadd = -1;
        else if (getFacingDirection() == Direction.RIGHT)
            xadd = 1;

        final Vec2 v2 = s.getVector();
        final Vec2 v1 = getVector();
        double new_distance = Math.sqrt(((v2.x - (v1.x + xadd)) * (v2.x - (v1.x + xadd))) + ((v2.y - (v1.y + yadd)) * (v2.y - (v1.y + yadd))));

        return new_distance < distance;
    }


    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Select this sprite to be the sprite the player will play as <br></br>
     * If the player is currently playing as another Sprite, then the {@link com.dissonance.framework.game.sprites.impl.PlayableSprite#onDeselect()} will be
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
