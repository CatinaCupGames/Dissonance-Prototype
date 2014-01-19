package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.sprites.animation.Animator;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteAnimationInfo;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.util.vector.Vector2f;

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
    private boolean movementDetect = false;
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
        } else
            throw new InvalidParameterException("An AnimatedSprite can only have a SpriteTexture!");
    }

    @Override
    public void onLoad() {
        ANIMATION_FACTORY_ID = AnimationFactory.queueAnimator(this);
        if (getTexture() != null) {
            setAnimation(0);
        }
    }

    @Override
    public void onUnload() {
        AnimationFactory.removeAnimator(ANIMATION_FACTORY_ID);
    }

    @Override
    public void setX(float x) {
        movementDetect = true;
        if (this.x > x) { //Left
            onMovement(Direction.LEFT);
        } else if (this.x < x) {
            onMovement(Direction.RIGHT);
        }
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        movementDetect = true;
        if (this.y > y) {
            onMovement(Direction.UP);
        } else if (this.y < y) {
            onMovement(Direction.DOWN);
        }
        super.setY(y);
    }

    protected void onMovement(Direction direction1) { }

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

    @Override
    public void render() {
	    //ROBO //todo fix all this shit
        if (texture == null)
            return;
        texture.bind();
        float bx = width / 2;
        float by = height / 2;
        final float x = getX(), y = getY();
        float z = 0f;
        //float z = (y - (by / 2));

        if (hasTint) {
            float alpha = 1;
            if (a < 1) {
                alpha = this.a - (1 - RenderService.getCurrentAlphaValue());
                if (alpha < 0)
                    alpha = 1;
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
        }
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
            texture.step();
            if (animationFrame != null) {
                animationFrame.onAnimationFrame(this);
            }
            if (animationFinished != null) {
                if (getCurrentFrame() == 0 || getCurrentFrame() == getFrameCount() - 1)
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