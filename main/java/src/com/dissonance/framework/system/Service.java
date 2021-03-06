package com.dissonance.framework.system;

import com.dissonance.framework.system.utils.Validator;

import java.util.*;

public abstract class Service {

    private boolean paused;

    private long serviceThreadID;

    private Runnable runnable;

    private boolean started;

    private boolean terminated;

    private final Queue<ServiceRunnable> listToRun = new LinkedList<>();

    private final List<Runnable> toRemove = new ArrayList<>();

    public void start() {
        if (!hasUpdate()) {
            onStart();
            return; //This service does not need to update, it may handle it on it's own.
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                serviceThreadID = Thread.currentThread().getId();
                onStart();
                started = true;
                Thread.currentThread().setName(getName());
                while (!terminated) {

                    // Flush all "Run on thread" actions //
                    ArrayList<ServiceRunnable> temp = new ArrayList<ServiceRunnable>();
                    synchronized (listToRun) {
                        while (!listToRun.isEmpty()) {
                            ServiceRunnable r = listToRun.poll();
                            try {
                                r.runnable.run();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                            if (r.everyTick && !r.killed)
                                temp.add(r);
                        }

                        if (toRemove.size() > 0) {
                            for (Runnable r : toRemove) {
                                Iterator<ServiceRunnable> iterator = temp.iterator();
                                while (iterator.hasNext()) {
                                    ServiceRunnable sr = iterator.next();
                                    if (sr.runnable == r)
                                        iterator.remove();
                                }
                            }
                            toRemove.clear();
                        }

                        listToRun.addAll(temp);
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

    protected boolean hasUpdate() {
        return true;
    }


    // === Functions === //

    public abstract void provideData(Object obj, int type);

    public ServiceRunnable runOnServiceThread(Runnable runnable) {
        return runOnServiceThread(runnable, false);
    }

    public ServiceRunnable runOnServiceThread(Runnable runnable, boolean force_next_frame) {
        return runOnServiceThread(runnable, force_next_frame, false);
    }

    public ServiceRunnable runOnServiceThread(Runnable runnable, boolean force_next_frame, boolean every_tick) {
        Validator.validateNotNull(runnable, "runnable");
        if (Thread.currentThread().getId() == serviceThreadID && !force_next_frame && !every_tick) //Run the runnable if were already on the service thread
            runnable.run();
        else { //Otherwise queue it
            synchronized (listToRun) {
                ServiceRunnable runnable1 = new ServiceRunnable();
                runnable1.runnable = runnable;
                runnable1.everyTick = every_tick;
                listToRun.offer(runnable1);
                return runnable1;
            }
        }
        return null;
    }

    public void removeServiceTick(Runnable runnable) {
        Validator.validateNotNull(runnable, "runnable");

        toRemove.add(runnable);
    }



    // === Properties === //

    public boolean isPaused() {
        return paused;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public boolean hasStarted() {
        return started;
    }

    public class ServiceRunnable {
        private boolean everyTick;
        private Runnable runnable;
        private boolean killed = false;

        public void kill() {
            killed = true;
        }
    }
}
