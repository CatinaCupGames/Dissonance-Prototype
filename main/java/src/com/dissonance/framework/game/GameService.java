package com.dissonance.framework.game;

import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.settings.SettingsLevelTest;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Validator;
import com.sun.istack.internal.NotNull;
import org.yaml.snakeyaml.Yaml;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class GameService {
    public static final String encryptAlgorithm = "PBEWithMD5AndDES";

    public static String[] args;
    private static long TID;
    private static boolean alive = true;
    private static Thread questThread;
    //private static final SoundSystem soundSystem = new SoundSystem();
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
            if (quest != null) {
                quest.setWorld(currentQuest.getWorld()); //Set the world to the last world used by the last quest
            }
        }
        if (!alive)
            return;
        //...what do we do now..?
    }

    //public static SoundSystem getSoundSystem() {
    //    return soundSystem;
    //}

    public static void handleKillRequest() {
        try {
            saveGame();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static void saveGame() throws Exception
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
    }


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
}
