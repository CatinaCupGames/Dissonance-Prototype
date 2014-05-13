package com.dissonance.framework.render;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public final class Camera {
    private static final float OFFSCREEN_THRESHOLD = 32;

    private static float posX;
    private static float posY;
    private static float[] bounds = new float[4];
    private static boolean ignoreBounds = true;
    private static boolean isEasing;
    private static boolean isMoving;
    private static boolean isLinear;
    private static Vector2f nextPos;
    private static Vector2f oldPos;
    private static long startTime;
    private static float duration;
    private static CameraMovementListener listener;
    private static Sprite follower;

    private static boolean isShaking;
    private static Direction shakeDir;
    private static long started;
    private static long shakeDuration;
    private static double shakeWidth;
    private static double shakeIntensity;
    private static float xShake, yShake;

    static {
        removeBounds();
    }

    public static void setBounds(int minx, int miny, int maxx, int maxy) {
        setBounds((float)minx, (float)miny, (float)maxx, (float)maxy);
    }

    public static void setBounds(float minx, float miny, float maxx, float maxy) {
        if (minx > maxx)
            throw new InvalidParameterException("The parameter \"minx\" is should not be greater than the parameter \"maxx\"");
        if (miny > maxy)
            throw new InvalidParameterException("The parameter \"miny\" is should not be greater than the parameter \"maxy\"");

        bounds[0] = minx;
        bounds[1] = miny;
        bounds[2] = maxx;
        bounds[3] = maxy;
        ignoreBounds = false;
    }

    public static void setBounds(Rectangle bounds) {
        setBounds(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
    }

    public static void removeBounds() {
        ignoreBounds = true;
    }

    public static float getX() {
        return posX + xShake;
    }

    public static void setX(float x) {
        if ((x >= bounds[0] && x <= bounds[2]) || ignoreBounds) {
            posX = x;
        } else if (x < bounds[0]) {
            posX = bounds[0];
        } else if (x > bounds[2]) {
            posX = bounds[2];
        }
    }

    public static float getY() {
        return posY + yShake;
    }

    public static void setY(float y) {
        if ((y >= bounds[1] && y <= bounds[3]) || ignoreBounds) {
            posY = y;
        } else if (y < bounds[1]) {
            posY = bounds[1];
        } else if (y > bounds[3]) {
            posY = bounds[3];
        }
    }

    public static void setPos(Vector2f pos) {
       if ((pos.x >= bounds[0] && pos.x <= bounds[2]) || ignoreBounds) {
           posX = pos.x;
       } else if (pos.x < bounds[0]) {
           posX = bounds[0];
       } else if (pos.x > bounds[2]) {
           posX = bounds[2];
       }

        if ((pos.y >= bounds[1] && pos.y <= bounds[3]) || ignoreBounds) {
            posY = pos.y;
        } else if (pos.y < bounds[1]) {
            posY = bounds[1];
        } else if (pos.y > bounds[3]) {
            posY = bounds[3];
        }
    }

    public static boolean isOffScreen(float x, float y, float width, float height) {
        return (x + width) - posX < -OFFSCREEN_THRESHOLD || Math.abs(posX - (x - width))  > OFFSCREEN_THRESHOLD + (GameSettings.Display.resolution.getWidth() / RenderService.ZOOM_SCALE) || (y + height) - posY < -OFFSCREEN_THRESHOLD || Math.abs(posY - (y - height)) > OFFSCREEN_THRESHOLD + (GameSettings.Display.resolution.getHeight() / RenderService.ZOOM_SCALE);
    }

    public static boolean isOffScreen(Sprite sprite) {
        return isOffScreen(sprite.getX(), sprite.getY(), sprite.getTexture().getTextureWidth() / 2, sprite.getTexture().getTextureHeight() / 2);
    }

    public static boolean isOffScreen(float x, float y) {
        return isOffScreen(x, y, 0, 0);
    }

    public static Vector2f translateToScreenCord(Vector2f vec) {
        float yadd = 0;
        if (GameSettings.Display.window_height > 1000) {
            yadd = 0.5f * GameSettings.Display.window_height-532;
        }
        vec.x = posX + vec.x;
        vec.y = posY + (vec.y + yadd);
        return vec;
    }

    public static Vector2f translateToCameraCenter(Vector2f vec, float height) {
        vec.x = (float) (vec.x - (GameSettings.Display.resolution.getWidth() / (RenderService.ZOOM_SCALE * 2f)));
        vec.y = (float) (vec.y - (GameSettings.Display.resolution.getHeight() / (RenderService.ZOOM_SCALE * 2f)) + height);
        return vec;
    }

    public static Vector2f translateToCameraCenter(Vector2f vec) {
        return translateToCameraCenter(vec, 0f);
    }

    public static void setCameraEaseListener(CameraMovementListener listener) {
        Camera.listener = listener;
    }

    public static void lerp(float newx, float newy, float duration) {
        easeMovement(new Vector2f(newx, newy), duration);
    }

    public static void followSprite(Sprite sprite) {
        if (follower != null) {
            follower.setSpriteMovedListener(null);
        }
        follower = sprite;
        if (follower == null)
            return;
        Camera.setPos(Camera.translateToCameraCenter(sprite.getVector(), sprite.getHeight()));
        follower.setSpriteMovedListener(new Sprite.SpriteEvent.SpriteMovedEvent() {
            @Override
            public void onSpriteMoved(Sprite sprite, float oldx, float oldy) {
                setPos(translateToCameraCenter(sprite.getVector(), 32));
            }
        });
    }

    public static void stopFollowing() {
        if (follower == null)
            return;
        followSprite(null);
    }

    public static void easeMovement(Vector2f newPos, float duration) {
        nextPos = newPos;
        oldPos = new Vector2f(getX(), getY());
        startTime = System.currentTimeMillis();
        if (isEasing)
            startTime -= 10;
        Camera.duration = duration;
        isEasing = true;
        isMoving = true;
        isLinear = false;
    }

    public static void linearMovement(Vector2f newPos, float duration) {
        nextPos = newPos;
        oldPos = new Vector2f(getX(), getY());
        startTime = System.currentTimeMillis();
        Camera.duration = duration;
        isEasing = false;
        isMoving = true;
        isLinear = true;
    }

    /**
     * Shake the camera.
     * This event is ran asynchronously on the render thread and the invokation of this method does not mean the completion of it.
     * To wait for this event to complete, invoke {@link Camera#waitForShakeEnd} <br></br>
     * This method invokes {@link Camera#shake(com.dissonance.framework.system.utils.Direction, long, double, double)} with the direction being {@link com.dissonance.framework.system.utils.Direction#UP}, the duration being <b>1.7 seconds</b>, the width being <b>20</b> and the intensity being <b>2</b>
     */
    public static void shake() {
        shake(Direction.UP, 1700L, 20, 2);
    }

    /**
     * Shake the camera.
     * This event is ran asynchronously on the render thread and the invokation of this method does not mean the completion of it.
     * To wait for this event to complete, invoke {@link Camera#waitForShakeEnd} <br></br>
     * This method invokes {@link Camera#shake(com.dissonance.framework.system.utils.Direction, long, double, double)} with the duration being <b>1.7 seconds</b>, the width being <b>20</b> and the intensity being <b>2</b>
     * @param direction The direction in which to shake the camera. The {@link com.dissonance.framework.system.utils.Direction#simple()} direction is used.
     */
    public static void shake(Direction direction) {
        shake(direction, 1700L, 20, 2);
    }

    /**
     * Shake the camera.
     * This event is ran asynchronously on the render thread and the invokation of this method does not mean the completion of it.
     * To wait for this event to complete, invoke {@link Camera#waitForShakeEnd} <br></br>
     * This method invokes {@link Camera#shake(com.dissonance.framework.system.utils.Direction, long, double, double)} with the width being <b>20</b> and the intensity being <b>2</b>
     * @param direction The direction in which to shake the camera. The {@link com.dissonance.framework.system.utils.Direction#simple()} direction is used.
     * @param duration How long to shake the camera, in ms.
     */
    public static void shake(Direction direction, long duration) {
        shake(direction, duration, 20, 2);
    }

    /**
     * Shake the camera.
     * This event is ran asynchronously on the render thread and the invokation of this method does not mean the completion of it.
     * To wait for this event to complete, invoke {@link Camera#waitForShakeEnd} <br></br>
     * This method invokes {@link Camera#shake(com.dissonance.framework.system.utils.Direction, long, double, double)} with the intensity being <b>2</b>
     * @param direction The direction in which to shake the camera. The {@link com.dissonance.framework.system.utils.Direction#simple()} direction is used.
     * @param duration How long to shake the camera, in ms.
     * @param width How wide the camera should shake, the higher the number, the shorter the width.
     */
    public static void shake(Direction direction, long duration, double width) {
        shake(direction, duration, width, 2);
    }

    /**
     * Shake the camera.
     * This event is ran asynchronously on the render thread and the invokation of this method does not mean the completion of it.
     * To wait for this event to complete, invoke {@link Camera#waitForShakeEnd}
     * @param direction The direction in which to shake the camera. The {@link com.dissonance.framework.system.utils.Direction#simple()} direction is used.
     * @param duration How long to shake the camera, in ms.
     * @param width How wide the camera should shake, the higher the number, the shorter the width.
     * @param intensity How intense the camera should shake, the higher the number, the lower the intensity.
     */
    public static void shake(Direction direction, long duration, double width, double intensity) {
        shakeDuration = duration;
        shakeDir = direction.simple();
        shakeWidth = width;
        shakeIntensity = intensity;

        started = RenderService.getTime();
        isShaking = true;
    }

    public static synchronized void waitForShakeEnd() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        pls.waitForEnd();
    }

    public static void easeMovementY(float newPos, float duration) {
        easeMovement(new Vector2f(getX(), newPos), duration);
    }

    public static void easeMovementX(float newPos, float duration) {
        easeMovement(new Vector2f(newPos, getY()), duration);
    }

    private static final WhatAmIDoing uwot = new WhatAmIDoing();
    private static final SHAKEIT pls = new SHAKEIT();
    public static void waitForEndOfMovement() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        uwot.waitForEnd();
    }

    private static void wakeUp() {
        uwot.wakeUp();
    }

    private static void doneShake() {
        pls.wakeUp();
    }

    static void executeAnimation() {
        if (isShaking) {
            float xadd = 0, yadd = 0;
            switch (shakeDir) {
                case UP:
                case DOWN:
                    yadd = (float) (Math.cos(System.currentTimeMillis() * shakeWidth) / shakeIntensity);
                    break;
                case LEFT:
                case RIGHT:
                    xadd = (float) (Math.cos(System.currentTimeMillis() * shakeWidth) / shakeIntensity);
                    break;
                default:
                    return;
            }

            xShake = xadd;
            yShake = yadd;
            if (RenderService.getTime() - started >= shakeDuration) {
                isShaking = false;
                xShake = 0f;
                yShake = 0f;
                doneShake();
            }
        }
        if (!isMoving)
            return;
        if (isLinear) {
            long time = System.currentTimeMillis() - startTime;
            float percent;
            if (time > duration) {
                percent = 1f;
            } else {
                percent = time / duration;
            }
            float x = oldPos.x + ((nextPos.x - oldPos.x) * percent);
            float y = oldPos.y + ((nextPos.y - oldPos.y) * percent);
            setX(x);
            setY(y);
            if (listener != null)
                listener.onMovement(x, y, time);
            if (x == nextPos.x && y == nextPos.y) {
                isMoving = false;
                isLinear = false;
                if (listener != null)
                    listener.onMovementFinished();
                wakeUp();
            }
        }
        else if (isEasing) {
            long time = System.currentTimeMillis() - startTime;
            float x = ease(oldPos.x, nextPos.x, duration, time);
            float y = ease(oldPos.y, nextPos.y, duration, time);
            setX(x);
            setY(y);
            if (listener != null)
                listener.onMovement(x, y, time);
            if (x == nextPos.x && y == nextPos.y) {
                isMoving = false;
                isEasing = false;
                if (listener != null)
                    listener.onMovementFinished();
                wakeUp();
            }
        }
    }

    //Code taken from: https://code.google.com/p/replicaisland/source/browse/trunk/src/com/replica/replicaisland/Lerp.java?r=5
    //Because I'm a no good dirty scrub
    public static float ease(float start, float target, float duration, float timeSinceStart) {
        float value = start;
        if (timeSinceStart > 0.0f && timeSinceStart < duration) {
            final float range = target - start;
            final float percent = timeSinceStart / (duration / 2.0f);
            if (percent < 1.0f) {
                value = start + ((range / 2.0f) * percent * percent * percent);
            } else {
                final float shiftedPercent = percent - 2.0f;
                value = start + ((range / 2.0f) *
                        ((shiftedPercent * shiftedPercent * shiftedPercent) + 2.0f));
            }
        } else if (timeSinceStart >= duration) {
            value = target;
        }
        return value;
    }

    public static void followPlayer() {
        followSprite(PlayableSprite.getCurrentlyPlayingSprite());
    }

    public interface CameraMovementListener {
        public void onMovement(float x, float y, long time);

        public void onMovementFinished();
    }

    private static class WhatAmIDoing {
        public synchronized void waitForEnd() throws InterruptedException {
            while (true) {
                if (!isEasing && !isLinear)
                    break;
                super.wait(0L);
            }
        }

        public synchronized void wakeUp() {
            super.notifyAll();
        }
    }

    private static class SHAKEIT {
        public synchronized void waitForEnd() throws InterruptedException {
            while (true) {
                if (!isShaking)
                    break;
                super.wait(0L);
            }
        }

        public synchronized void wakeUp() {
            super.notifyAll();
        }
    }
}
