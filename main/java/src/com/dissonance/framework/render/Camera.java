package com.dissonance.framework.render;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import org.lwjgl.util.vector.Vector2f;

public final class Camera {
    private static final float OFFSCREEN_THRESHOLD = 32;

    private static float posX;
    private static float posY;
    private static boolean isEasing;
    private static boolean isMoving;
    private static boolean isLinear;
    private static Vector2f nextPos;
    private static Vector2f oldPos;
    private static long startTime;
    private static float duration;
    private static CameraMovementListener listener;
    private static Sprite follower;

    public static float getX() {
        return posX;
    }

    public static void setX(float x) {
        if (((GameService.getCurrentWorld() == null && x >= 0) || (GameService.getCurrentWorld() != null && x >= GameService.getCurrentWorld().getTiledData().getTileWidth())) &&
                (GameService.getCurrentWorld() == null || x <= ( ((float)(GameService.getCurrentWorld().getTiledData().getPixelWidth()) - (GameSettings.Display.resolution.getWidth() / RenderService.ZOOM_SCALE))) - GameService.getCurrentWorld().getTiledData().getTileWidth()))
            posX = x;
    }

    public static float getY() {
        return posY;
    }

    public static void setY(float y) {
        if (((GameService.getCurrentWorld() == null && y >= 0) || (GameService.getCurrentWorld() != null && y >= -GameService.getCurrentWorld().getTiledData().getTileHeight())) &&
                (GameService.getCurrentWorld() == null || y <= ( ((float)(GameService.getCurrentWorld().getTiledData().getPixelHeight()) - (GameSettings.Display.resolution.getHeight() / RenderService.ZOOM_SCALE))) - GameService.getCurrentWorld().getTiledData().getTileHeight()))
            posY = y;
    }

    public static void setPos(Vector2f pos) {
        if (((GameService.getCurrentWorld() == null && pos.x >= 0) || (GameService.getCurrentWorld() != null && pos.x >= GameService.getCurrentWorld().getTiledData().getTileWidth())) &&
                (GameService.getCurrentWorld() == null || pos.x <= ( ((float)(GameService.getCurrentWorld().getTiledData().getPixelWidth()) - (GameSettings.Display.resolution.getWidth() / RenderService.ZOOM_SCALE))) - GameService.getCurrentWorld().getTiledData().getTileWidth()))
            posX = pos.x;

        if (((GameService.getCurrentWorld() == null && pos.y >= 0) || (GameService.getCurrentWorld() != null && pos.y >= -GameService.getCurrentWorld().getTiledData().getTileHeight())) &&
                (GameService.getCurrentWorld() == null || pos.y <= ( ((float)(GameService.getCurrentWorld().getTiledData().getPixelHeight()) - (GameSettings.Display.resolution.getHeight() / RenderService.ZOOM_SCALE))) - GameService.getCurrentWorld().getTiledData().getTileHeight()))
            posY = pos.y;
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

    public static void easeMovementY(float newPos, float duration) {
        easeMovement(new Vector2f(getX(), newPos), duration);
    }

    public static void easeMovementX(float newPos, float duration) {
        easeMovement(new Vector2f(newPos, getY()), duration);
    }

    private static final WhatAmIDoing uwot = new WhatAmIDoing();
    public static void waitForEndOfMovement() throws InterruptedException {
        uwot.waitForEnd();
    }

    private static void wakeUp() {
        uwot.wakeUp();
    }

    static void executeAnimation() {
        if (!isMoving)
            return;
        if (isLinear) {
            long time = System.currentTimeMillis() - startTime;
            float percent;
            if (time > duration) {
                percent = 1;
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
}
