package com.dissonance.framework.render;

public interface Drawable extends Comparable<Drawable> {
    public static final int BEFORE = -1;
    public static final int EQUAL = 0;
    public static final int AFTER = 1;

    public void init();

    public void update();

    public void render();
}
