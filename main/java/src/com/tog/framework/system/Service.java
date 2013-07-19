package com.tog.framework.system;

public interface Service extends Runnable {
    public Thread start();

    public void pause();

    public void resume();

    public void terminate();

    public void provideData(Object obj, int type);

    public void runOnServiceThread(Runnable runnable);
}
