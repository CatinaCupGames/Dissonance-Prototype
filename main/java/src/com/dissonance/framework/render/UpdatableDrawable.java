package com.dissonance.framework.render;

import com.dissonance.framework.system.annotations.OpenglSafe;

public interface UpdatableDrawable extends Drawable {

    @OpenglSafe
    public void init();

    @OpenglSafe
    public void update();
}
