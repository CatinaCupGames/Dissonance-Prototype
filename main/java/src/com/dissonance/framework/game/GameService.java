package com.dissonance.framework.game;

import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Validator;
import com.dissonance.framework.system.utils.WinRegistry;
import com.sun.istack.internal.NotNull;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class GameService {
    static {
        String lwjgl_folder = "libs" + File.separator + "lwjgl_native" + File.separator;
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if (OS.contains("win"))
            lwjgl_folder += "windows";
        else if (OS.contains("mac"))
            lwjgl_folder += "macosx";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            lwjgl_folder += "linux";
        else if (OS.contains("sunos"))
            lwjgl_folder += "solaris";
        System.setProperty("org.lwjgl.librarypath", new File(lwjgl_folder).getAbsolutePath());
        System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
    }
    public static boolean coop_mode = false;
    public static final String encryptAlgorithm = "PBEWithMD5AndDES";

    public static String[] args;
    private static long TID;
    private static boolean alive = true;
    private static Thread questThread;
    private static AbstractQuest currentQuest;

    public static void beginQuest(@NotNull AbstractQuest quest) {
        Validator.validateNotNull(quest, "quest");
        if (RenderService.isInRenderThread())
            throw new RuntimeException("The Quest Thread cannot be the same thread as the RenderService Thread!");
        Thread.currentThread().setName("QuestHandler Thread");
        while (quest != null && alive) {
            TID = Thread.currentThread().getId();
            currentQuest = quest;
            currentQuest.setNextQuest((AbstractQuest) null);
            questThread = new Thread(questRun);
            questThread.start();
            System.out.println("[GameService] QUEST " + quest.getClass().getCanonicalName() + " STARTED");
            try {
                System.out.println("[GameService] Waiting for end");
                currentQuest.waitForEnd();
                System.out.println("[GameService] Quest Ended");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            quest = currentQuest.getNextQuest();
            if (quest != null && currentQuest.getWorld() != null) {
                quest.setWorld(currentQuest.getWorld()); //Set the world to the last world used by the last quest
            }
        }
        if (!alive)
            return;
        //...what do we do now..?
    }

    public static void loadEssentials(String[] args) throws IOException {
        GameService.args = args;
        for (String s : args) {
            if (s.equals("--debug") || s.equals("-d"))
                Debug.debugMode(true);
        }
        System.out.println("Using libs folder " + System.getProperty("org.lwjgl.librarypath"));
        System.out.println("Loading Input config");
        InputKeys.initializeConfig();
        System.out.println("Loading game settings");
        GameSettings.loadGameSettings();
        System.out.println("Loading game dialog");
        DialogFactory.loadDialog();
        System.out.println("Loading sound");
        Sound.loadAllSounds();
        System.out.println("Setting sticky keys to 0");
        /*
        HKEY_CURRENT_USER\Control Panel\Accessibility\StickyKeys
         */
        /*try {
            WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, "Control Panel\\Accessibility\\StickyKeys", "Flags", "506");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }

    public static void handleKillRequest() {
        alive = false;
        try {
            questThread.interrupt();
            currentQuest.endQuest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (DialogUI.currentDialogBox() != null) {
            DialogUI.currentDialogBox().endDialog();
        }

        Sound.dispose();
    }

    public static World getCurrentWorld() {
        return currentQuest.getWorld();
    }

    public static long getGameServiceThreadID() {
        return TID;
    }

    public static Thread getQuestThread() {
        return questThread;
    }

    /*public static void saveGame() throws Exception
    {
        Map<Object, Object> topLevel;
        Map<Object, Object> settingsLevel;
        Map<Object, Object> windowLevel;
        Map<Object, Object> renderLevel;
        Map<Object, Object> displayLevel;
        Map<Object, Object> colorLevel;
        Map<Object, Object> playerLevel;
        topLevel = new HashMap<Object, Object>();
        {
            settingsLevel = new HashMap<Object, Object>();
            {
                windowLevel = new HashMap<Object, Object>();
                renderLevel = new HashMap<Object, Object>();
                {
                    displayLevel = new HashMap<Object, Object>();
                    colorLevel = new HashMap<Object, Object>();
                }
            }
            playerLevel = new HashMap<Object, Object>();
        }

        topLevel.put("settings", settingsLevel);
        {
            settingsLevel.put("window", windowLevel);
            {
                windowLevel.put("size", new Integer[] {GameSettings.Display.window_width, GameSettings.Display.window_height});
                windowLevel.put("fullscreen", GameSettings.Display.fullscreen);
            }
            settingsLevel.put("render", renderLevel);
            {
                renderLevel.put("display", displayLevel);
                {
                    int resW = (int)GameSettings.Display.resolution.getWidth();
                    int resH = (int)GameSettings.Display.resolution.getHeight();
                    double[] ar = GameSettings.Display.resolution.aspectRatio.getAspectRatio();
                    int[] aspectRatio = new int[] {(int)ar[0], (int)ar[1]};

                    displayLevel.put("monitor", 0);
                    displayLevel.put("resolution", new Integer[] {resW, resH});
                    displayLevel.put("aspect-ratio", new Integer[] {aspectRatio[0], aspectRatio[1]});
                }
                renderLevel.put("color", colorLevel);
                {
                    colorLevel.put("brightness", GameSettings.Graphics.color.brightness);
                    colorLevel.put("contrast", GameSettings.Graphics.color.contrast);
                    colorLevel.put("saturation", GameSettings.Graphics.color.saturation);
                    colorLevel.put("balance", new Float[] {GameSettings.Graphics.color.red, GameSettings.Graphics.color.green, GameSettings.Graphics.color.blue});
                }
            }
        }
        topLevel.put("player", playerLevel);
        {
            PlayableSprite ps = PlayableSprite.getCurrentlyPlayingSprite();

            playerLevel.put("name", ps.getSpriteName());
            playerLevel.put("level", ps.getLevel());
            playerLevel.put("attack", ps.getAttack());
            playerLevel.put("defense", ps.getDefense());
            playerLevel.put("speed", ps.getSpeed());
            playerLevel.put("vigor", ps.getVigor());
            playerLevel.put("stamina", ps.getStamina());
            playerLevel.put("willpower", ps.getWillPower());
            playerLevel.put("focus", ps.getFocus());
            playerLevel.put("marksmanship", ps.getMarksmanship());
            playerLevel.put("magicresistance", ps.getMagicResistance());
            playerLevel.put("powers", new String[] {"+Speed, +Agility, -Stamina"});
        }

        SettingsLevelTest slt = new SettingsLevelTest();
        topLevel.put("test", slt);

        Yaml yaml = new Yaml();

        yaml.setName("test");
        yaml.dump(topLevel, new FileWriter("config/save.yml"));

        FileInputStream fis = new FileInputStream("config/save.yml");
        FileOutputStream fos = new FileOutputStream("config/save.dat");

        byte k[] = "HignDIPs".getBytes();
        SecretKeySpec key = new SecretKeySpec(k, encryptAlgorithm.split("/")[0]);
        Cipher encrypt = Cipher.getInstance(encryptAlgorithm);
        encrypt.init(Cipher.ENCRYPT_MODE, key);
        CipherOutputStream cos = new CipherOutputStream(fos, encrypt);

        byte[] buf = new byte[2048];
        int read;
        while((read = fis.read(buf)) != -1)
        {
            cos.write(buf, 0, read);
        }

        fis.close();
        cos.flush();
        cos.close();
    }*/


    private static final Runnable questRun = new Runnable() {

        @Override
        public void run() {
            Thread.currentThread().setName("Quest Thread");
            try {
                currentQuest.startQuest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static AbstractQuest getCurrentQuest() {
        return currentQuest;
    }
}
