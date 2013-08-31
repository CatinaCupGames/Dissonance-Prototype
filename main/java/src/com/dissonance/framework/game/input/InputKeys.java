package com.dissonance.framework.game.input;

import com.dissonance.framework.system.utils.Validator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputKeys {
    private final static String DIR = "config" + File.separator;
    private final static String PATH = DIR + "keyconfig.txt"; //move if ya want
    public final static HashMap<String, Integer> keys = new HashMap<>();

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

        for (String string : lines) {
            String[] split = string.split(":");
            Validator.validateNotBelow(split.length, 2, "key config property value");

            keys.put(split[0], Integer.parseInt(split[1]));
        }
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

    public static int getMoveUpKey() {
        return keys.get("moveUp");
    }

    public static int getMoveLeftKey() {
        return keys.get("moveLeft");
    }

    public static int getMoveDownKey() {
        return keys.get("moveDown");
    }

    public static int getMoveRightKey() {
        return keys.get("moveRight");
    }

    public static int getJumpKey() {
        return keys.get("jump");
    }

    public static int getDodgeKey() {
        return keys.get("dodge");
    }

    public static int getExtendLookKey() {
        return keys.get("extendLook");
    }

    public static int getStrafeKey() {
        return keys.get("strafe");
    }

    public static int getAttackKey() {
        return keys.get("attack");
    }

    public static int getSpecialKey() {
        return keys.get("special");
    }

    public static int getMagic1Key() {
        return keys.get("magic1");
    }

    public static int getMagic2Key() {
        return keys.get("magic2");
    }

    public static int getMenuKey() {
        return keys.get("menu");
    }

    public static int getPauseKey() {
        return keys.get("pause");
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