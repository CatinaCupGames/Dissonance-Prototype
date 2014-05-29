package com.dissonance.framework.game.player.input.joypad;

import com.dissonance.framework.game.player.ControllerInput;
import com.dissonance.framework.game.player.Input;
import com.dissonance.framework.system.utils.FileUtils;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class Joypad {
    private static final File DEFAULT_CONFIG = new File("config/defaultController.dat");
    private Controller controller;
    private boolean isLoaded;
    //jump, 1
    private HashMap<String, String> ids = new HashMap<String, String>();
    //z, +z
    private HashMap<String, String> analog = new HashMap<String, String>();

    private HashMap<String, Component> componentCache = new HashMap<String, Component>();

    Joypad(Controller controller) {
        this.controller = controller;
    }

    private boolean loadSetup() {
        File file = new File("config/" + controller.getName() + ".dat");
        if (!file.exists()) {
            System.err.println("No controller config found for controller \"" + controller.getName() + "\"");
            System.err.println("Attempting to use default configuration..");
            file = DEFAULT_CONFIG;
        }

        try {
            String[] lines = FileUtils.readAllLines(file.getAbsolutePath());
            for (String s : lines) {
                if (s.split(";").length == 2) {
                    String gid = s.split(";")[0];
                    String cid = s.split(";")[1];
                    if (cid.startsWith("+") || cid.startsWith("-")) {
                        String temp = cid;
                        cid = cid.substring(1);
                        analog.put(gid, temp);
                    }
                    ids.put(gid, cid);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean load() {
        isLoaded = loadSetup();
        return isLoaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public Controller getController() {
        return controller;
    }

    public Input createInput() {
        if (isLoaded) {
            return new ControllerInput(this);
        } else {
            //try to load it?
            load();
            if (!isLoaded)
                throw new InvalidParameterException("Joypad can't be loaded! Device: \"" + controller.getName() + "\"");
            return new ControllerInput(this);
        }
    }

    public boolean isButtonPressed(String value) {
        Component component = getComponentFor(value);
        if (component == null)
            return false;
        if (analog.containsKey(value)) {
            String temp = analog.get(value);

            if (temp.startsWith("+")) {
                return component.getPollData() > 0.5f;
            } else if (temp.startsWith("-")) {
                return component.getPollData() < -0.5f;
            }
        }
        return component.getPollData() == 1.0f;
    }

    public Component getComponentFor(String value) {
        if (ids.containsKey(value)) {
            String cid = ids.get(value);
            return getComponent(cid);
        }
        return null;
    }

    public Component getComponent(String id) {
        if (componentCache.containsKey(id))
            return componentCache.get(id);
        for (Component c : controller.getComponents()) {
            if (c.getIdentifier().getName().equals(id)) {
                componentCache.put(id, c);
                return c;
            }
        }
        return null;
    }
}
