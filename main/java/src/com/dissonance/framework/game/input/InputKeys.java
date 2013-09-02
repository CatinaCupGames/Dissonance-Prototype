package com.dissonance.framework.game.input;

import com.dissonance.framework.system.utils.Validator;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputKeys {
    public static final String MOVEUP = "moveUp";
    public static final String MOVEDOWN = "moveDown";
    public static final String MOVELEFT = "moveLeft";
    public static final String MOVERIGHT = "moveRight";
    public static final String PAUSE = "pause";
    public static final String MENU = "menu";
    public static final String MAGIC1 = "magic1";
    public static final String MAGIC2 = "magic2";
    public static final String SPECIAL = "special";
    public static final String ATTACK = "attack";
    public static final String STRAFE = "strafe";
    public static final String JUMP = "jump";
    public static final String DODGE = "dodge";
    public static final String EXTENDLOOK = "extendLook";

    private final static String DIR = "config" + File.separator;
    private final static String PATH = DIR + "keyconfig.txt"; //move if ya want
    private final static HashMap<String, Integer> keys = new HashMap<>();
    private final static HashMap<String, String> buttons = new HashMap<>();
    private static Controller controller;

    private static String[] readAllLines(String filePath) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();

        return lines.toArray(new String[lines.size()]);
    }
    private static void writeAllLines(String filePath, String[] lines) throws IOException {
        FileOutputStream file = new FileOutputStream(filePath);
        for (String line : lines) {
            file.write((line + "\r\n").getBytes());
        }
        file.close();
    }

    public static void initializeConfig() throws IOException {
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        if (!new File(PATH).exists()) {
            createConfig();
        }

        String[] lines = readAllLines(PATH);
        Component[] components = null;
        for (String string : lines) {
            String[] split = string.split(":");
            Validator.validateNotBelow(split.length, 2, "key config property value");
            if (split[0].equals("cname")) {
                String cname = split[1];
                for (Controller c : getAllControllers()) {
                    if (c.getName().equals(cname) && c.getType() == Controller.Type.GAMEPAD) {
                        controller = c;
                        components = c.getComponents();
                        break;
                    }
                }
            } else {
                try {
                    keys.put(split[0], Integer.parseInt(split[1]));
                } catch (Throwable t) {
                    if (components == null)
                        continue;

                    for (Component c : components) {
                        if (c.getName().equals(split[1])) {
                            buttons.put(split[0], c.getName());
                            break;
                        }
                    }
                }
            }
        }
    }

    public static Controller[] getAllControllers() {
        return ControllerEnvironment.getDefaultEnvironment().getControllers();
    }

    private static void createConfig() throws IOException {
        String[] config = {
                "moveUp:17", "moveLeft:30", "moveDown:31", "moveRight:32",
                "jump:57", "dodge:42", "extendLook:23", "strafe:29",
                "attack:36", "special:37", "magic1:24", "magic2:38",
                "menu:15", "pause:1"
        };

        writeAllLines(PATH, config);
    }

    public static void setKey(String button, int key) {
        keys.put(button, key);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setButton(String button, Component component) {
        buttons.put(button, component.getName());
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() throws IOException {
        String[] config = new String[keys.size() + buttons.size() + (controller == null ? 0 : 1)];
        int i = 0;
        if (controller != null) {
            config[i] = "cname:" + controller.getName();
            i++;
        }
        for (String s : keys.keySet()) {
            config[i] = s + ":" + keys.get(s);
            i++;
        }
        for (String s : buttons.keySet()) {
            config[i] = s + ":" + buttons.get(s);
            i++;
        }

        writeAllLines(PATH, config);
    }

    public static boolean isButtonPressed(String button) {
        if (usingController()) {
            if (button.equals(MOVEUP) || button.equals(MOVELEFT) || button.equals(MOVERIGHT) || button.equals(MOVELEFT)) {
                throw new IllegalArgumentException("The player is using the controller! Use the axis!");
            }

            boolean valid = controller.poll();
            if (!valid) {
                checkKeyboard(button);
            }
            String cname = buttons.get(button);
            for (Component c : controller.getComponents()) {
                if (c.getName().equals(cname)) {
                    return cname.contains("Button") && c.getPollData() == 1.0f;
                }
            }
        }
        return checkKeyboard(button);
    }

    //TODO Create method to get axis data for controller

    private static boolean checkKeyboard(String button) {
        return Keyboard.isKeyDown(keys.get(button));
    }

    public static boolean usingController() {
        return controller != null && controllerConnected();
    }

    private static boolean controllerConnected() {
        if (controller == null)
            return false;
        try {
            return controller.poll();
        } catch (Exception e) {
            return false;
        }
    }

    public static int getMoveUpKey() {
        return keys.get(MOVEUP);
    }

    public static int getMoveLeftKey() {
        return keys.get(MOVELEFT);
    }

    public static int getMoveDownKey() {
        return keys.get(MOVEDOWN);
    }

    public static int getMoveRightKey() {
        return keys.get(MOVERIGHT);
    }

    public static int getJumpKey() {
        return keys.get(JUMP);
    }

    public static int getDodgeKey() {
        return keys.get(DODGE);
    }

    public static int getExtendLookKey() {
        return keys.get(EXTENDLOOK);
    }

    public static int getStrafeKey() {
        return keys.get(STRAFE);
    }

    public static int getAttackKey() {
        return keys.get(ATTACK);
    }

    public static int getSpecialKey() {
        return keys.get(SPECIAL);
    }

    public static int getMagic1Key() {
        return keys.get(MAGIC1);
    }

    public static int getMagic2Key() {
        return keys.get(MAGIC2);
    }

    public static int getMenuKey() {
        return keys.get(MENU);
    }

    public static int getPauseKey() {
        return keys.get(PAUSE);
    }
}
/*
 -W,S,A,D: move
  -Space: jump
  -Shift(with movement key): dodge
  -I(hold then use movement keys): extended look
  -Ctrl: toggle strafe
  -J: attack/select
  -K: special ability
  -O: magic 1
  -L: magic 2
  -Tab: equipment/status/magic menu
  -Esc: pause
*/