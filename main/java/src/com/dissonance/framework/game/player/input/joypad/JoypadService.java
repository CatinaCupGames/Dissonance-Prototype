package com.dissonance.framework.game.player.input.joypad;

import com.dissonance.framework.system.Service;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

import java.util.ArrayList;
import java.util.Iterator;

public class JoypadService extends Service {
    private ArrayList<Joypad> controllers = new ArrayList<>();
    private ControllerServiceListener listener;

    public void setServiceListener(ControllerServiceListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
        ControllerEnvironment.getDefaultEnvironment().addControllerListener(CLISTENER);

        for (Controller c : ca) {
            if (c.getType() == Controller.Type.GAMEPAD) {
                addController(c);
            }
        }
    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onTerminated() {
        ControllerEnvironment.getDefaultEnvironment().removeControllerListener(CLISTENER);
        controllers.clear();
    }

    @Override
    protected void onUpdate() { }

    @Override
    public void provideData(Object obj, int type) {

    }

    @Override
    protected boolean hasUpdate() {
        return false;
    }

    public Joypad[] getJoypads() {
        return controllers.toArray(new Joypad[controllers.size()]);
    }

    private void addController(Controller c) {
        Joypad joypad = new Joypad(c);
        controllers.add(joypad);
        if (listener != null)
            listener.onNewJoypad(joypad);
    }

    private void removeController(Controller c) {
        Iterator<Joypad> iterator = controllers.iterator();
        while (iterator.hasNext()) {
            Joypad j = iterator.next();
            if (j.getController().equals(c)) {
                iterator.remove();
                if (listener != null)
                    listener.onJoypadRemoved(j);
                return;
            }
        }
    }

    private final ControllerListener CLISTENER = new ControllerListener() {
        @Override
        public void controllerRemoved(ControllerEvent controllerEvent) {
            removeController(controllerEvent.getController());
        }

        @Override
        public void controllerAdded(ControllerEvent controllerEvent) {
            addController(controllerEvent.getController());
        }
    };

    public static interface ControllerServiceListener {
        public void onNewJoypad(Joypad joypad);

        public void onJoypadRemoved(Joypad joypad);
    }
}
