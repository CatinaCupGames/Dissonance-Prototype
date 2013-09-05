package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.sprites.animation.Animator;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteAnimationInfo;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class AnimatedSprite extends Sprite implements Animator {
    protected transient int ANIMATION_FACTORY_ID;
    private transient SpriteAnimationInfo animation;
    private transient int speed;


    @Override
    public void setTexture(Texture texture) {
        if (texture instanceof SpriteTexture) {
            super.setTexture(texture);
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
        if (getTexture() != null) {
            final SpriteTexture texture = (SpriteTexture)getTexture();
            SpriteAnimationInfo ani;
            if ((ani = texture.setCurrentAnimation(name)) != null) {
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
        if (getTexture() == null)
            return;
        final SpriteTexture texture = (SpriteTexture)getTexture();
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
    public void update() {
    }

    @Override
    public void init() {}

    private boolean paused;
    public void pauseAnimation() {
        paused = true;
    }

    public void playAnimation() {
        paused = false;
    }

    public boolean isAnimationPaused() {
        return paused;
    }

    public void setFrame(int frame_num) {
        ((SpriteTexture)getTexture()).setCurrentFrame(frame_num);
    }

    @Override
    public void onAnimate() {
        if (paused)
            return;
        if (getTexture() != null)
            ((SpriteTexture)getTexture()).step();
    }

    @Override
    public synchronized final void waitForAnimationEnd() {
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
