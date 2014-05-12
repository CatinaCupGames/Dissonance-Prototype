package com.dissonance.framework.game.player.input.joypad;

import com.dissonance.framework.game.player.ControllerInput;
import com.dissonance.framework.game.player.Input;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.system.utils.FileUtils;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.intellij.lang.annotations.MagicConstant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Joypad {
    private Controller controller;
    private boolean isLoaded;
    private HashMap<String, String> ids = new HashMap<String, String>();

    Joypad(Controller controller) {
        this.controller = controller;
    }

    private boolean loadSetup() {
        File file = new File("config/" + controller.getName() + ".dat");
        if (!file.exists())
            return false;

        try {
            String[] lines = FileUtils.readAllLines(file.getAbsolutePath());
            for (String s : lines) {
                if (s.split(";").length == 2) {
                    String gid = s.split(";")[0];
                    String cid = s.split(";")[1];
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
                return null;
            return new ControllerInput(this);
        }
    }

    public boolean isButtonPressed(String value) {
        Component component = getComponentFor(value);
        return component.getPollData() == 1.0f;
    }

    public Component getComponentFor(String value) {
        if (ids.containsKey(value)) {
            String cid = ids.get(value);
            for (Component c : controller.getComponents()) {
                if (c.getIdentifier().getName().equals(cid))
                    return c;
            }
        }
        return null;
    }
}
