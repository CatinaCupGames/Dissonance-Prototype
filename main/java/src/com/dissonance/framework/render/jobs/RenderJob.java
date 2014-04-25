package com.dissonance.framework.render.jobs;

import com.dissonance.framework.render.Drawable;

public interface RenderJob {

    public void add(Object... objs);

    public void preRender();

    public void render(Drawable d);

    public void postRender();
}
