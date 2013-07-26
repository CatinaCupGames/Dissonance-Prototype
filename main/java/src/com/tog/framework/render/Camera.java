package com.tog.framework.render;

public class Camera {
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


}
