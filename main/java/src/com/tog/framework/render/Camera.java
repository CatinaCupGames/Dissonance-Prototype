package com.tog.framework.render;

import org.lwjgl.util.vector.Vector2f;

public final class Camera {
    private static float posX;
    private static float posY;

    public static float getX() {
        return posX;
    }

    public static float getY() {
        return posY;
    }

    public static void setX(float x) {
        posX = x;
    }

    public static void setY(float y) {
        posY = y;
    }

    public static void setPos(Vector2f pos) {
        posX = pos.getX();
        posY = pos.getY();
    }

    public static Vector2f translateToCameraCenter(Vector2f vec, float width, float height) {
        vec.setX(vec.getX() - (RenderService.GAME_WIDTH / 4.5f) + width);
        vec.setY(vec.getY() - (RenderService.GAME_HEIGHT / 4.5f) + height);
        return vec;
    }

    public static Vector2f translateToCameraCenter(Vector2f vec) {
        return translateToCameraCenter(vec, 0f, 0f);
    }



    private static boolean isEasing;
    private static Vector2f nextPos;
    private static Vector2f oldPos;
    private static long startTime;
    private static float duration;
    private static CameraEaseListener listener;

    public static void setCameraEaseListener(CameraEaseListener listener) {
        Camera.listener = listener;
    }

    public static void lerp(float newx, float newy, float duration) {
        easeMovement(new Vector2f(newx, newy), duration);
    }

    public static void easeMovement(Vector2f newPos, float duration) {
        nextPos = newPos;
        oldPos = new Vector2f(getX(), getY());
        startTime = System.currentTimeMillis();
        if (isEasing)
            startTime -= 10;
        Camera.duration = duration;
        isEasing = true;
    }

    public static void easeMovementY(float newPos, float duration) {
        easeMovement(new Vector2f(getX(), newPos), duration);
    }

    public static void easeMovementX(float newPos, float duration) {
        easeMovement(new Vector2f(newPos, getY()), duration);
    }

    static void executeEase() {
        if (!isEasing)
            return;
        long time = System.currentTimeMillis() - startTime;
        float x = ease(oldPos.getX(), nextPos.getX(), duration, time);
        float y = ease(oldPos.getY(), nextPos.getY(), duration, time);
        setX(x);
        setY(y);
        if (listener != null)
            listener.onEase(x, y, time);
        if (x == nextPos.getX() && y == nextPos.getY()) {
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
