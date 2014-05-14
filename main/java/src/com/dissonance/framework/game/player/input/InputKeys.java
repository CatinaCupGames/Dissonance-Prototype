package com.dissonance.framework.game.player.input;

import com.dissonance.framework.system.utils.FileUtils;
import com.dissonance.framework.system.utils.Validator;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class InputKeys {
    public static final String MOVEX      = "mx";
    public static final String MOVEY      = "my";
    public static final String EXTENDX    = "ex";
    public static final String EXTENDY    = "ey";
    public static final String EXTENDUP   = "extendUp";
    public static final String EXTENDDOWN = "extendDown";
    public static final String EXTENDLEFT = "extendLeft";
    public static final String EXTENDRIGHT = "extendRight";
    public static final String MOVEUP     = "moveUp";
    public static final String MOVEDOWN   = "moveDown";
    public static final String MOVELEFT   = "moveLeft";
    public static final String MOVERIGHT  = "moveRight";
    public static final String PAUSE      = "pause";
    public static final String MENU       = "menu";
    public static final String MAGIC1     = "magic1";
    public static final String MAGIC2     = "magic2";
    public static final String SPECIAL    = "special";
    public static final String ATTACK     = "attack";
    public static final String STRAFE     = "strafe";
    public static final String SELECT     = "select";
    public static final String DODGE      = "dodge";
    public static final String SWITCH     = "switch";
    public static final String GAMEMENU   = "menu";
    public static final String QMENU      = "qmenu";


    private final static String DIR = "config" + File.separator;
    private static boolean loaded = false;
    private final static String CONFIG_NAME = "keyboard.dat";
    private final static String PATH = DIR + CONFIG_NAME; //move if ya want
    private final static HashMap<String, Integer> keys = new HashMap<>();

    public static void initializeConfig() throws IOException {
        loaded = true;
        FileUtils.createIfNotExist(DIR, CONFIG_NAME, createConfig());
        String[] lines = FileUtils.readAllLines(PATH);

        for (String string : lines) {
            String[] split = string.split(":");
            Validator.validateNotBelow(split.length, 2, "key config property value");
            try {
                keys.put(split[0], Integer.parseInt(split[1]));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static Controller[] getAllControllers() {
        return ControllerEnvironment.getDefaultEnvironment().getControllers();
    }

    private static String createConfig() throws IOException {
        return "moveUp:17\nmoveLeft:30\nmoveDown:31\nmoveRight:32\n" +
                "select:57\ndodge:42\nswitch:23\nstrafe:29\nattack:36\n" +
                "special:37\nmagic1:24\nmagic2:38\nmenu:15\npause:1\n" +
                "extendUp:200\nextendDown:208\nextendLeft:203\nextendRight:205\n";
    }

    public static void setKey(String button, int key) {
        keys.put(button, key);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static void setButton(String button, Component component) {
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static void setController(String name) {
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static void setController(Controller controller) {
    }

    public static void save() throws IOException {
        String[] config = new String[keys.size()];
        int i = 0;
        for (String s : keys.keySet()) {
            config[i] = s + ":" + keys.get(s);
            i++;
        }

        FileUtils.writeLines(PATH, config);
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static boolean isButtonPressed(String button) {
        if (!loaded)
            return false;
        /*if (usingController()) {
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
        }*/
        return checkKeyboard(button);
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static float getJoypadValue(String button) { return 0f; }

    public static float getJoypadValue(String button, Controller controller) {
        switch (button) {
            case MOVEX:
            case MOVELEFT:
            case MOVERIGHT:
                return controller.getComponent(getLeftPad()[0]).getPollData();
            case MOVEY:
            case MOVEUP:
            case MOVEDOWN:
                return controller.getComponent(getLeftPad()[1]).getPollData();
            case EXTENDX:
                return controller.getComponent(getRightPad()[0]).getPollData();
            case EXTENDY:
                return controller.getComponent(getRightPad()[1]).getPollData();
            default:
                return 0f;
        }
    }

    private static Component.Identifier.Axis[] getLeftPad() {
        return new Component.Identifier.Axis[] { Component.Identifier.Axis.X, Component.Identifier.Axis.Y };
    }

    private static Component.Identifier.Axis[] getRightPad() {
        return new Component.Identifier.Axis[] { Component.Identifier.Axis.RX, Component.Identifier.Axis.RY };
    }

    public static boolean checkKeyboard(String button) {
        return Keyboard.isKeyDown(keys.get(button));
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static boolean checkController(String button, Controller controller) {
        return false;
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    public static boolean usingController() {
        return false;
    }

    /**
     * @deprecated This method is deprecated, please see the co-op wiki for more info.
     */
    @Deprecated
    private static boolean controllerConnected() {
        return false;
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
        return keys.get(SELECT);
    }

    public static int getDodgeKey() {
        return keys.get(DODGE);
    }

    public static int getExtendLookKey() {
        return keys.get(SWITCH);
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