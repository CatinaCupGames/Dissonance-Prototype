package com.tog.framework.game.input;

import com.tog.framework.system.Service;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * The InputService class manages the game's input. <br />
 * Classes listening for input should pass in a {@link InputListener} to
 * this class in order to listen for specific mouse and keyboard input.
 */
public final class InputService implements Service {
    public static final int ADD_LISTENER = 0;
    public static final int REMOVE_LISTENER = 1;

    private List<Runnable> runnables = new ArrayList<>();

    private Thread serviceThread;
    private List<InputListener> listeners = new ArrayList<>();
    private boolean paused = false;

    @Override
    public Thread start() {
        try {
            if (!Keyboard.isCreated()) {
                Keyboard.create();
            }

            if (!Mouse.isCreated()) {
                Mouse.create();
            }

            if (!Keyboard.areRepeatEventsEnabled()) {
                Keyboard.enableRepeatEvents(true);
            }

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        keyboardThread = new Thread(keyboardRunnable);
        mouseThread = new Thread(mouseRunnable);

        serviceThread = new Thread(this);
        serviceThread.start();

        return serviceThread;
    }

    private Thread keyboardThread;
    private Thread mouseThread;
    private Runnable keyboardRunnable = new Runnable() {
        @Override
        public void run() {

            while (!paused) {
                Keyboard.next();

                if (!Keyboard.getEventKeyState()) {
                    continue;
                }

                int key = Keyboard.getEventKey();

                for (InputListener listener : listeners) {
                    if (listener.getKeys().contains(key)) {
                        listener.inputPressed(key);
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable mouseRunnable = new Runnable() {
        @Override
        public void run() {
            while (!paused) {
                Mouse.next();

                if (!Mouse.getEventButtonState()) {
                    continue;
                }

                int button = Mouse.getEventButton();

                for (InputListener listener : listeners) {
                    if (listener.getButtons().contains(button)) {
                        listener.inputClicked(button);
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void pause() {
        if (paused) {
            throw new IllegalStateException("This service is already paused!");
        }

        paused = true;

        mouseThread.interrupt();
        keyboardThread.interrupt();
    }

    @Override
    public void resume() {
        if (!paused) {
            throw new IllegalStateException("This service isn't paused!");
        }

        paused = false;

        mouseThread.start();
        keyboardThread.start();
    }

    @Override
    public void terminate() {
        serviceThread.interrupt();
        try {
            serviceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        serviceThread = null;
        listeners = null;
    }

    @Override
    public void provideData(Object obj, int type) {
        Validator.validateNotNull(obj, "object");
        Validator.validateInterface(obj, InputListener.class);

        InputListener listener = (InputListener) obj;

        if (type == ADD_LISTENER) {
            if (listeners.contains(listener)) {
                throw new IllegalArgumentException("The specified listener has already been added!");
            }

            listeners.add(listener);

            if (listeners.size() > 0 && paused) {
                resume();
            }
        } else if (type == REMOVE_LISTENER) {
            if (!listeners.contains(listener)) {
                throw new IllegalArgumentException("The specified listener hasn't been added yet!");
            }

            listeners.remove(listener);

            if (listeners.size() == 0 && !paused) {
                pause();
            }
        }
    }

    @Override
    public void runOnServiceThread(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void run() {
        mouseThread.start();
        keyboardThread.start();

        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }
}
