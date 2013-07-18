package com.tog.framework.system;

public interface Service extends Runnable {
    public Thread start();

    public void pause();

    public void resume();

    public void terminate();
}
