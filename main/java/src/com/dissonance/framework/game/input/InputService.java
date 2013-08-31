package com.dissonance.framework.game.input;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.utils.Validator;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Rumbler;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static Map<String, Controller> controllers = new HashMap<String, Controller>();
    private static Map<Controller, Rumbler[]> rumblers = new HashMap<Controller, Rumbler[]>();

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
                            //System.out.println(PRESSED);
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

            loadControllers();

        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        renderService = (RenderService)ServiceManager.getService(RenderService.class);
        keyboardThread = new Thread(keyboardRunnable);
        mouseThread = new Thread(mouseRunnable);


        mouseThread.start();
        keyboardThread.start();

        // Start getting data.
        //for(int i = 0; i < controllers.size(); i++)
        //{
        //    controllers.values().toArray(new Controller[controllers.size()])[i].poll();
        //}
    }

    private void loadControllers() {
        Controller[] controllers1 = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(final Controller controller2 : controllers1) {
            if(controller2.getType() == Controller.Type.GAMEPAD
                    // This is just for testing due to the fact that I only have a wheel.
                    || controller2.getType() == Controller.Type.WHEEL) {
                controllers.put(controller2.getName(), controller2);
                rumblers.put(controller2, controller2.getRumblers());

                //TEMP CODE
                System.out.println(controller2.getName() + " : " + controller2.getPortNumber() + " : " + controller2.getPortType() + " : " + controller2.getType());
                for(final Rumbler rumbler : controller2.getRumblers()) {
                    System.out.println("  - " + rumbler.getAxisName() + " : " + rumbler.getAxisIdentifier());
                }
            }
        }
    }

    private static Object getValue(Component component)
    {
        Component.Identifier identifier = component.getIdentifier();
        float data = component.getPollData();

        Object returnValue = null;

        if(identifier instanceof Component.Identifier.Axis)
        {
            returnValue = component.getPollData();
        } else if(identifier instanceof Component.Identifier.Button) {
            returnValue = false;
            if(data == 1.0f)
            {
                returnValue = true;
            }
        } else if(identifier instanceof Component.Identifier.Key) {
            // Just in case. For example the Xbox controller keypad plug-in.
            returnValue = false;
            if(data == 1.0f)
            {
                returnValue = true;
            }
        }

        return returnValue;
    }

    public static float getAxisValue(String controller, String component)
    {
        Controller controller1 = controllers.get(controller);
        controller1.poll();
        Component[] components = controller1.getComponents();

        float returnValue = 0.0f;
        for(final Component component1 : components)
        {
            if(component1.getName().equalsIgnoreCase(component))
            {
                returnValue = (float)getValue(component1);
            }
        }

        return returnValue;
    }

    public static float getAxisValue(String controller, Component.Identifier componentId)
    {
        Controller controller1 = controllers.get(controller);
        controller1.poll();
        Component component = controller1.getComponent(componentId);

        float returnValue = (float)getValue(component);

        return returnValue;
    }

    public static boolean getButtonState(String controller, String component)
    {
        Controller controller1 = controllers.get(controller);
        controller1.poll();
        Component[] components = controller1.getComponents();

        boolean returnValue = false;
        for(final Component component1 : components)
        {
            if(component1.getName().equalsIgnoreCase(component))
            {
                returnValue = (boolean)getValue(component1);
            }
        }

        return returnValue;
    }

    public static boolean getButtonState(String controller, Component.Identifier componentId)
    {
        Controller controller1 = controllers.get(controller);
        controller1.poll();
        Component component = controller1.getComponent(componentId);

        boolean returnValue = (boolean)getValue(component);

        return returnValue;
    }

    public static boolean getKeyState(String controller, String component)
    {
        return getButtonState(controller, component);
    }

    public static boolean getKeyState(String controller, Component.Identifier componentId)
    {
        Controller controller1 = controllers.get(controller);

        return getButtonState(controller, componentId);
    }

    public static void debugController(String controller)
    {
        Controller controller1 = controllers.get(controller);
        StringBuffer buffer = new StringBuffer();

        Component component;
        for(int i = 0; i < controller1.getComponents().length; i++)
        {
            component = controller1.getComponents()[i];

            buffer.append(component.getName());
            buffer.append(":");
            buffer.append(component.getIdentifier());
            buffer.append(":");
            if(component.isAnalog())
            {
                buffer.append(component.getPollData());
            } else {
                if(component.getPollData() == 1.0f)
                {
                    buffer.append("true");
                } else {
                    buffer.append("false");
                }
            }
            buffer.append("\n");
        }

        System.out.println(buffer.toString());
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