package com.dissonance.framework.render;

import com.dissonance.framework.system.annotations.OpenGLSafe;

public interface UpdatableDrawable extends Drawable {

    @OpenGLSafe
    public void init();

    @OpenGLSafe
    public void update();
}
