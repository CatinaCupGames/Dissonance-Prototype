package com.dissonance.framework.render;

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

    public static Vec2 translateToScreenCord(Vec2 vec) {
        return translateToScreenCord(vec, 2.5f);
    }

    public static Vec2 translateToScreenCord(Vec2 vec, float scale) {
        vec.x = (posX + (-vec.x + (RenderService.GAME_WIDTH / scale)));
        vec.y = (posY + (-vec.y + (RenderService.GAME_HEIGHT / scale)));
        return vec;
    }

    public static Vec2 translateToCameraCenter(Vec2 vec, float width, float height) {
        vec.x = (vec.x - (RenderService.GAME_WIDTH / 4.5f) + width);
        vec.y = (vec.y - (RenderService.GAME_HEIGHT / 4.5f) + height);
        return vec;
    }

    public static Vec2 translateToCameraCenter(Vec2 vec) {
        return translateToCameraCenter(vec, 0f, 0f);
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
}
