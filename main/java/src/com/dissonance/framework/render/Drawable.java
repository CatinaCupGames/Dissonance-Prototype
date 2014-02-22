package com.dissonance.framework.render;

import com.dissonance.framework.system.utils.openglsafe.OpenGLSafe;

public interface Drawable extends Comparable<Drawable> {
    public static final int BEFORE = -1;
    public static final int EQUAL = 0;
    public static final int AFTER = 1;

    @OpenGLSafe
    public void render();

    public float getX();

    public float getY();

    public float getWidth();

    public float getHeight();
}
