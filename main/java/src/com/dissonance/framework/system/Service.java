package com.dissonance.framework.system;

import com.dissonance.framework.system.utils.Validator;

import java.util.*;

public abstract class Service {

    private boolean paused;

    private long serviceThreadID;

    private Runnable runnable;

    private boolean terminated;

    private final Queue<Runnable> listToRun = new LinkedList<>();

    public void start() {
        runnable = new Runnable() {
            @Override
            public void run() {
                serviceThreadID = Thread.currentThread().getId();
                onStart();
                Thread.currentThread().setName(getName());
                while (!terminated) {

                    // Flush all "Run on thread" actions //
                    while (!listToRun.isEmpty()) {
                        Runnable r = listToRun.poll();
                        try {
                            r.run();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }

                    update();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        new Thread(runnable).start();
    }

    public void pause() {
        if (paused)
            throw new IllegalStateException("This service is already paused!");
        paused = true;

        runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                onPause();
            }
        });

    }

    public void resume() {
        if (!paused)
            throw new IllegalStateException("This service is not paused!");
        paused = false;

        runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                onResume();
            }
        });
    }

    public void terminate() {
        if (terminated)
            throw new IllegalStateException("This service is already terminated!");
        terminated = true;
        onTerminated();
    }

    private void update() {
        if (paused) return;
        onUpdate();
    }


    // === Events === //

    protected abstract void onStart();

    protected abstract void onPause();

    protected abstract void onResume();

    protected abstract void onTerminated();

    protected abstract void onUpdate();

    protected String getName() {
        return getClass().getSimpleName();
    }


    // === Functions === //

    public abstract void provideData(Object obj, int type);

    public void runOnServiceThread(Runnable runnable) {
        runOnServiceThread(runnable, false);
    }

    public void runOnServiceThread(Runnable runnable, boolean force_next_frame) {
        Validator.validateNotNull(runnable, "runnable");
        if (Thread.currentThread().getId() == serviceThreadID && !force_next_frame) //Run the runnable if were already on the service thread
            runnable.run();
        else { //Otherwise queue it
            synchronized (listToRun) {
                listToRun.offer(runnable);
            }
        }
    }


    // === Properties === //

    public boolean isPaused() {
        return paused;
    }

    public boolean isTerminated() {
        return terminated;
    }
}
