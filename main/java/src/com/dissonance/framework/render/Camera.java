package com.dissonance.framework.render;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.game.sprites.Sprite;
import org.jbox2d.common.Vec2;

public final class Camera {
    private static float posX;
    private static float posY;
    private static boolean isEasing;
    private static Vec2 nextPos;
    private static Vec2 oldPos;
    private static long startTime;
    private static float duration;
    private static CameraEaseListener listener;

    public static float getX() {
        return posX;
    }

    public static void setX(float x) {
        posX = x;
    }

    public static float getY() {
        return posY;
    }

    public static void setY(float y) {
        posY = y;
    }

    public static void setPos(Vec2 pos) {
        posX = pos.x;
        posY = pos.y;
    }

    public static boolean isOffScreen(float x, float y, float width, float height, float scale) {
        return (x + width) - posX < 0 || Math.abs(posX - (x - width))  > GameSettings.Display.window_width / scale || (y + height) - posY < 0 || Math.abs(posY - (y - height)) > GameSettings.Display.window_height / scale;
    }

    public static boolean isOffScreen(Sprite sprite, float scale) {
        return isOffScreen(sprite.getX(), sprite.getY(), sprite.getTexture().getTextureWidth() / 2, sprite.getTexture().getTextureHeight() / 2, scale);
    }

    public static boolean isOffScreen(float x, float y, float scale) {
        return isOffScreen(x, y, 0, 0, scale);
    }

    public static Vec2 translateToScreenCord(Vec2 vec) {
        return translateToScreenCord(vec, 2f);
    }

    public static Vec2 translateToScreenCord(Vec2 vec, float scale) {
        vec.x = (posX + (-vec.x + (GameSettings.Display.window_width / scale)));
        vec.y = (posY + (-vec.y + (GameSettings.Display.window_height / scale)));
        return vec;
    }

    public static Vec2 translateToCameraCenter(Vec2 vec, float height) {
        vec.x = (vec.x - (GameSettings.Display.window_width / 4f));
        vec.y = (vec.y - (GameSettings.Display.window_height / 4f) + height);
        return vec;
    }

    public static Vec2 translateToCameraCenter(Vec2 vec) {
        return translateToCameraCenter(vec, 0f);
    }

    public static void setCameraEaseListener(CameraEaseListener listener) {
        Camera.listener = listener;
    }

    public static void lerp(float newx, float newy, float duration) {
        easeMovement(new Vec2(newx, newy), duration);
    }

    public static void easeMovement(Vec2 newPos, float duration) {
        nextPos = newPos;
        oldPos = new Vec2(getX(), getY());
        startTime = System.currentTimeMillis();
        if (isEasing)
            startTime -= 10;
        Camera.duration = duration;
        isEasing = true;
    }

    public static void easeMovementY(float newPos, float duration) {
        easeMovement(new Vec2(getX(), newPos), duration);
    }

    public static void easeMovementX(float newPos, float duration) {
        easeMovement(new Vec2(newPos, getY()), duration);
    }

    private static final WhatAmIDoing uwot = new WhatAmIDoing();
    public static void waitForEndOfEase() throws InterruptedException {
        uwot.waitForEnd();
    }

    private static void wakeUp() {
        uwot.wakeUp();
    }

    static void executeEase() {
        if (!isEasing)
            return;
        long time = System.currentTimeMillis() - startTime;
        float x = ease(oldPos.x, nextPos.x, duration, time);
        float y = ease(oldPos.y, nextPos.y, duration, time);
        setX(x);
        setY(y);
        if (listener != null)
            listener.onEase(x, y, time);
        if (x == nextPos.x && y == nextPos.y) {
            isEasing = false;
            if (listener != null)
                listener.onEaseFinished();
            wakeUp();
        }
    }

    //Code taken from: https://code.google.com/p/replicaisland/source/browse/trunk/src/com/replica/replicaisland/Lerp.java?r=5
    //Because I'm a no good dirty scrub
    private static float ease(float start, float target, float duration, float timeSinceStart) {
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

    public interface CameraEaseListener {
        public void onEase(float x, float y, long time);

        public void onEaseFinished();
    }

    private static class WhatAmIDoing {
        public synchronized void waitForEnd() throws InterruptedException {
            while (true) {
                if (!isEasing)
                    break;
                super.wait(0L);
            }
        }

        public synchronized void wakeUp() {
            super.notifyAll();
        }
    }
}
