package com.dissonance.framework.render;

import com.dissonance.framework.game.world.World;

public interface Renderer {
      public void start();

    public void render();

    public void terminate();


    public void fadeToBlack(float speed);

    public void fadeFromBlack(float speed);

    public void fadeToAlpha(float speed, float alpha);

    public void waitForFade() throws InterruptedException;

    public boolean isFading();

    public boolean isCrossFading();

    public float getCurrentAlphaValue();


    public void removeScale();

    public void resetScale();


    public void provideData(Object obj, int type);

    public World getCurrentDrawingWorld();
}
