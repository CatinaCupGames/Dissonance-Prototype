package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.sprites.animation.Animator;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteAnimationInfo;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class AnimatedSprite extends UpdatableSprite implements Animator {
    private AnimatedSpriteEvent.OnAnimationPlayEvent animationPlayEvent;
    private AnimatedSpriteEvent.OnAnimationPauseEvent animationPauseEvent;
    private AnimatedSpriteEvent.OnAnimationFinished animationFinished;
    private AnimatedSpriteEvent.OnAnimationFrame animationFrame;
    private SpriteTexture texture;

    protected transient int ANIMATION_FACTORY_ID;
    private transient SpriteAnimationInfo animation;
    private transient int speed;
    private int nMovementCount = 0;
    protected boolean movementDetect = false;
    private float oX, oY;

    /**
     * Sets this {@link AnimatedSprite AnimatedSprite's}
     * {@link AnimatedSpriteEvent.OnAnimationPauseEvent OnAnimationPauseEvent listener} to the specified listener.
     *
     * @param animationPlayListener The new event listener.
     */
    public void setAnimationPlayListener(AnimatedSpriteEvent.OnAnimationPlayEvent animationPlayListener) {
        this.animationPlayEvent = animationPlayListener;
    }

    public void setAnimationFinishedListener(AnimatedSpriteEvent.OnAnimationFinished animationFinished) {
        this.animationFinished = animationFinished;
    }

    public void setAnimationFrameListener(AnimatedSpriteEvent.OnAnimationFrame animationFrame) {
        this.animationFrame = animationFrame;
    }

    /**
     * Sets this {@link AnimatedSprite AnimatedSprite's}
     * {@link AnimatedSpriteEvent.OnAnimationPlayEvent OnAnimationPlayEvent listener} to the specified listener.
     *
     * @param animationPauseListener The new event listener.
     */
    public void setAnimationPauseListener(AnimatedSpriteEvent.OnAnimationPauseEvent animationPauseListener) {
        this.animationPauseEvent = animationPauseListener;
    }

    @Override
    public void setTexture(Texture texture) {
        if (texture instanceof SpriteTexture) {
            super.setTexture(texture);
            this.texture = (SpriteTexture)texture;
            width = texture.getWidth();
            height = texture.getHeight();

            setCutOffMargin(getHeight() / 2f);
        } else
            throw new InvalidParameterException("An AnimatedSprite can only have a SpriteTexture!");
    }

    @Override
    public void onLoad() {
        ANIMATION_FACTORY_ID = AnimationFactory.queueAnimator(this);
        if (getTexture() != null) {
            setAnimation(0);
        }
        super.onLoad();
    }

    @Override
    public void onUnload() {
        AnimationFactory.removeAnimator(ANIMATION_FACTORY_ID);
    }

    private long lastX, lastY;
    private Direction lastDirX, lastDirY;

    @Override
    public void setX(float x) {
        movementDetect = true;
        if (this.x > x) { //Left
            long dur = System.currentTimeMillis() - lastY;
            onMovement(dur < 50 ? Direction.LEFT.add(lastDirY) : Direction.LEFT);
            lastDirX = Direction.LEFT;
        } else if (this.x < x) {
            long dur = System.currentTimeMillis() - lastY;
            onMovement(dur < 50 ? Direction.RIGHT.add(lastDirY) : Direction.RIGHT);
            lastDirX = Direction.RIGHT;
        }
        lastX = System.currentTimeMillis();
        super.setX(x);
    }

    /**
     * Change the X position of this AnimatedSprite without checking for animation
     * direction
     * @param x The X position to set this animated sprites x to.
     */
    public void rawSetX(float x) {
        super.setX(x);
    }

    /**
     * Change the Y position of this AnimatedSprite without checking for animation
     * direction
     * @param y The Y position to set this animated sprites x to.
     */
    public void rawSetY(float y) {
        super.setY(y);
    }

    @Override
    public void setY(float y) {
        movementDetect = true;
        if (this.y > y) {
            long dur = System.currentTimeMillis() - lastX;
            onMovement(dur < 50 ? Direction.UP.add(lastDirX) : Direction.UP);
            lastDirY = Direction.UP;
        } else if (this.y < y) {
            long dur = System.currentTimeMillis() - lastX;
            onMovement(dur < 50 ? Direction.DOWN.add(lastDirX) : Direction.DOWN);
            lastDirY = Direction.DOWN;
        }
        lastY = System.currentTimeMillis();
        super.setY(y);
    }

    /**
     * This method is invoked when movement has been detected or requested. This method should change and play the current animation depending on the
     * direction <b>passed in the parameter.</b>
     * @param direction1 The direction this sprite should be facing.
     */
    protected void onMovement(Direction direction1) { }

    /**
     * This method is invoked when no movement has been detected or requested. This method should pause the current animation and set it's frame to the idle frame of the
     * current animation.
     */
    protected void onNoMovement() { }

    public abstract String getSpriteName();

    public boolean setAnimation(String name) {
        if (animation != null && animation.getName().equals(name))
            return true;
        if (texture != null) {
            SpriteAnimationInfo ani;
            if ((ani = texture.setCurrentAnimation(name)) != null) {
                this.animation = ani;
                speed = (int)ani.getDefaultSpeed();
                AnimationFactory.resetAnimator(ANIMATION_FACTORY_ID);
                return true;
            }
        }
        return false;
    }

    public boolean setAnimation(int row) {
        if (texture != null) {
            SpriteAnimationInfo ani;
            if ((ani = texture.setCurrentAnimation(row)) != null) {
                this.animation = ani;
                speed = (int)ani.getDefaultSpeed();
                AnimationFactory.resetAnimator(ANIMATION_FACTORY_ID);
                return true;
            }
        }
        return false;
    }

    public SpriteAnimationInfo getCurrentAnimation() {
        return animation;
    }

    public ToastText toastText(String text) {
        ToastText toast = new ToastText(this, text, 750);
        getWorld().loadAndAdd(toast);

        return toast;
    }

    public ToastText toastText(String text, float duration) {
        ToastText toast = new ToastText(this, text, duration);
        getWorld().loadAndAdd(toast);

        return toast;
    }

    public ToastText toastText(String text, float duration, Color color) {
        ToastText toast = new ToastText(this, text, duration);
        toast.setTint(color);
        getWorld().loadAndAdd(toast);

        return toast;
    }

    @Override
    public void render() {
        if (!visible)
            return;
        if (texture == null)
            return;
        if (glowing) {
            super.renderGlow();
        }
        texture.bind();
        float bx = width / 2;
        float by = height / 2;
        final float x = getX(), y = getY();
        float z = 0f;
        //float z = (y - (by / 2));

        if (hasTint) {
            float alpha = RenderService.getCurrentAlphaValue();
            if (a < 1) {
                alpha = this.a - (1 - RenderService.getCurrentAlphaValue());
                if (alpha < 0)
                    alpha = 0;
            }
            glColor4f(r, g, b, alpha);
        }


        Vector2f bl = texture.getTextureCord(SpriteTexture.BOTTOM_LEFT);
        Vector2f br = texture.getTextureCord(SpriteTexture.BOTTOM_RIGHT);
        Vector2f ur = texture.getTextureCord(SpriteTexture.TOP_RIGHT);
        Vector2f ul = texture.getTextureCord(SpriteTexture.TOP_LEFT);
        glBegin(GL_QUADS);
        glTexCoord2f(bl.getX(), bl.getY()); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(br.getX(), br.getY()); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(ur.getX(), ur.getY()); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(ul.getX(), ul.getY()); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
        //glColor3f(1f, 1f, 1f);
    }

    @Override
    public void init() {}

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (movementDetect) {
            if (oX == -1 && oY == -1) {
                oX = this.x;
                oY = this.y;
            } else if (oX == this.x && oY == this.y) {
                nMovementCount++;
                if (nMovementCount >= 3) {
                    movementDetect = false;
                    onNoMovement();
                    oX = -1;
                    oY = -1;
                }
            } else {
                nMovementCount = 0;
                oX = -1;
                oY = -1;
            }
        } else if (!isAnimationPaused() && !movementDetect)
            movementDetect = true;
    }

    private boolean paused;
    public void pauseAnimation() {
        if (animationPauseEvent != null) {
            animationPauseEvent.onAnimationPause(this);
        }

        paused = true;
    }

    public void playAnimation() {
        if (animationPlayEvent != null) {
            animationPlayEvent.onAnimationPlay(this);
        }

        paused = false;
    }

    public boolean isAnimationPaused() {
        return paused;
    }

    public void setFrame(int frame_num) {
        if (texture == null)
            return;
        texture.setCurrentFrame(frame_num);
    }

    public int getCurrentFrame() {
        if (texture == null)
            return -1;
        return texture.getCurrentStep();
    }

    @Override
    public void onAnimate() {
        if (paused)
            return;
        if (texture != null) {
            int lastFrame = getCurrentFrame();
            texture.step();
            if (animationFrame != null) {
                animationFrame.onAnimationFrame(this);
            }
            if (animationFinished != null) {
                if (getCurrentFrame() == 0 || getCurrentFrame() == lastFrame) //If it looped back or if it didn't advance
                    animationFinished.onAnimationFinished(this);
            }
        }
    }

    @Override
    public synchronized final void waitForAnimationEnd() {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        try {
            super.wait(0L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized final void wakeUp() {
        super.notify();
    }

    @Override
    public int getAnimationSpeed() {
        return speed;
    }

    @Override
    public int getFrameCount() {
        if (animation != null)
            return animation.size();
        return -1;
    }

    public void setAnimationSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Tell the sprite to animate its movement animation
     */
    public void animateMovement() {
        onMovement(getFacingDirection());
    }

    /**
     * Change which way the sprite is facing. This method depends on the child sprite to have the correct implementation
     * of {@link com.dissonance.framework.game.sprites.impl.AnimatedSprite#onMovement(com.dissonance.framework.system.utils.Direction)} and of {@link AnimatedSprite#onNoMovement()} <br></br>
     *
     * @param direction The direction to face
     */
    public void face(Direction direction) {
        setFacingDirection(direction);
        animateMovement(); //Tell the sprite to change it's current animation
        onNoMovement(); //Then tell it it's actually not moving
    }

    public interface AnimatedSpriteEvent {
        /**
         * Interface definition for a callback to be invoked when the {@link AnimatedSprite AnimatedSprite's} animation
         * has been resumed.
         */
        public interface OnAnimationPlayEvent {
            public void onAnimationPlay(AnimatedSprite sprite);
        }

        /**
         * Interface definition for a callback to be invoked when the {@link AnimatedSprite AnimatedSprite's} animation
         * has been paused.
         */
        public interface OnAnimationPauseEvent {
            public void onAnimationPause(AnimatedSprite sprite);
        }

        public interface OnAnimationFinished {
            public void onAnimationFinished(AnimatedSprite sprite);
        }

        public interface OnAnimationFrame {
            public void onAnimationFrame(AnimatedSprite sprite);
        }
    }

}