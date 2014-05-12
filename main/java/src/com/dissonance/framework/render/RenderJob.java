package com.dissonance.framework.render;

public interface RenderJob {

    public void startFrame();

    public void executeJob();

    public void endFrame();
}
