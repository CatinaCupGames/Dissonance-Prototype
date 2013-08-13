package com.tog.framework.game.input;

import com.tog.framework.render.RenderService;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.swing.plaf.TableHeaderUI;
import java.util.ArrayList;
import java.util.List;

/**
 * The InputService class manages the game's input.<br />
 * Classes listening for input should pass in a {@link InputListener} to
 * this class in order to listen for specific mouse and keyboard input.
 */
public final class InputService extends Service {
    /**
     * A type for the {@link #provideData(Object, int)} method.<br />
     * The object passed in should be an {@link InputListener}. The
     * specified listener will be added to the list of input listeners.
     */
    public static final int ADD_LISTENER = 0;

    /**
     * A type for the {@link #provideData(Object, int)} method.<br />
     * The object passed in should be an {@link InputListener}. The
     * specified listener will be removed from the list of input listeners.
     */
    public static final int REMOVE_LISTENER = 1;

    private boolean[] PRESSED = new boolean[255];
    private RenderService renderService;
    private final List<Runnable> runnables = new ArrayList<>();

    private Thread serviceThread;
    private List<InputListener> listeners = new ArrayList<>();
    private boolean paused = false;

    private Thread keyboardThread;
    private Thread mouseThread;
    private Runnable keyboardRunnable = new Runnable() {
        @Override
        public void run() {

            while (!paused) {
                if (!renderService.isTerminated() && listeners != null) {
                    for (InputListener listener : listeners) {
                        for (Integer i : listener.getKeys()) {
                            boolean keyDown = Keyboard.isKeyDown(i);
                            PRESSED[i - 1] = keyDown;
                        }
                    }
                }
                else {
                    return;
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        }
    };

    private Runnable mouseRunnable = new Runnable() {
        @Override
        public void run() {
            while (!paused) {
                if (!renderService.isTerminated()  && listeners != null) {
                    Mouse.next();

                    if (!Mouse.getEventButtonState()) {
                        continue;
                    }

                    int button = Mouse.getEventButton();

                    for (InputListener listener : listeners) {
                        if (listener.getButtons().contains(button)) {
                            listener.inputClicked(button, Mouse.getX(), Mouse.getY());
                        }
                    }
                }
                else {
                    return;
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onStart() {
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

        renderService = (RenderService)ServiceManager.getService(RenderService.class);
        keyboardThread = new Thread(keyboardRunnable);
        mouseThread = new Thread(mouseRunnable);


        mouseThread.start();
        keyboardThread.start();
    }

    @Override
    protected void onPause() {
        mouseThread.interrupt();
        keyboardThread.interrupt();
    }

    @Override
    protected void onResume() {
        mouseThread.start();
        keyboardThread.start();
        serviceThread.start();
    }

    @Override
    protected void onTerminated() {
        if(serviceThread != null) serviceThread.interrupt();
        if(mouseThread != null) mouseThread.interrupt();
        if(keyboardThread != null) keyboardThread.interrupt();
        serviceThread = null;
        mouseThread = null;
        keyboardThread = null;
        listeners = null;
    }

    @Override
    protected void onUpdate() {
        for (Runnable runnable : runnables) {
            runnable.run();
        }

        if (listeners.size() > 0) {
            for (InputListener listener : listeners) {
                for (Integer i : listener.getKeys()) {
                    if (PRESSED[i - 1])
                        listener.inputPressed(i);
                }
            }
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
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


}