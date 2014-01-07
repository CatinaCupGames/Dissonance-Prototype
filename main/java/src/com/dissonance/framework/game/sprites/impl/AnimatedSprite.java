package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.sprites.animation.Animator;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteAnimationInfo;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
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
        } else
            throw new InvalidParameterException("An AnimatedSprite can only have a SpriteTexture!");
    }

    @Override
    public void onLoad() {
        ANIMATION_FACTORY_ID = AnimationFactory.queueAnimator(this);
        if (getTexture() != null && ((SpriteTexture)getTexture()).getCurrentAnimation() != null) {
            animation = ((SpriteTexture)getTexture()).getCurrentAnimation();
            speed = (int)animation.getDefaultSpeed();
        }
    }

    @Override
    public void onUnload() {
        AnimationFactory.removeAnimator(ANIMATION_FACTORY_ID);
    }

    public abstract String getSpriteName();

    public void setAnimation(String name) {
        if (texture != null) {
            SpriteAnimationInfo ani;
            if ((ani = texture.setCurrentAnimation(name)) != null) {
                this.animation = ani;
                speed = (int)ani.getDefaultSpeed();
            }
        }
    }

    public void setAnimation(int row) {
        if (texture != null) {
            SpriteAnimationInfo ani;
            if ((ani = texture.setCurrentAnimation(row)) != null) {
                this.animation = ani;
                speed = (int)ani.getDefaultSpeed();
            }
        }
    }

    public SpriteAnimationInfo getCurrentAnimation() {
        return animation;
    }

    @Override
    public void render() {
	    //ROBO //todo fix all this shit
        if (getTexture() == null)
            return;
        texture.bind();
        float bx = texture.getWidth() / 2;
        float by = texture.getHeight() / 2;
        final float x = getX(), y = getY();

        //glColor4f(0f, 0f, 0f, 1f); //DEBUG LINE FOR TEXTURES
        Vector2f bl = texture.getTextureCord(SpriteTexture.BOTTOM_LEFT);
        Vector2f br = texture.getTextureCord(SpriteTexture.BOTTOM_RIGHT);
        Vector2f ur = texture.getTextureCord(SpriteTexture.TOP_RIGHT);
        Vector2f ul = texture.getTextureCord(SpriteTexture.TOP_LEFT);
        glBegin(GL_QUADS);
        glTexCoord2f(bl.getX(), bl.getY()); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(br.getX(), br.getY()); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(ur.getX(), ur.getY()); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(ul.getX(), ul.getY()); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        texture.unbind();
        //glColor3f(1f, 1f, 1f);
    }

    @Override
    public void init() {}

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