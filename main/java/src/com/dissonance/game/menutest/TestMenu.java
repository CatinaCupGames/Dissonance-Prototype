package com.dissonance.game.menutest;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.ticker.Ticker;
import com.dissonance.game.sprites.TestPlayer;
import org.oyasunadev.li.liui.component.gui.CMenu;
import org.oyasunadev.li.liui.component.gui.MenuItem;

import java.io.File;
import java.util.Locale;

public class TestMenu
{
    public static void main(String[] args) throws Exception
    {
        new TestMenu();
    }

    private TestMenu() throws Exception
    {
        System.out.println("Loading INPUT CONFIGURATION...");
        InputKeys.initializeConfig();
        System.out.println("Loaded INPUT CONFIGURATION!");

        System.out.println("Loading GAME DIALOG...");
        InputKeys.initializeConfig();
        System.out.println("Loaded GAME DIALOG!");

        System.out.println("Starting GAME...");
        GameService.beginQuest(new TestQuest());
        System.out.println("Closing GAME!");
    }

    private static final Ticker TICKER = new Ticker();
    private static boolean started;

    public static void startGame()
    {
        if(started)
        {
            return;
        }

        started = true;
    }

    public static Ticker getSystemTicker()
    {
        return TICKER;
    }

    static
    {
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

    private class TestQuest extends AbstractQuest
    {
        private TestQuest()
        {
        }

        @Override
        public void startQuest()
        {
            try {
                World world = WorldFactory.getWorld("test_world");
                setWorld(world);
            } catch (WorldLoadFailedException e) {
                e.printStackTrace();
            }

            // Quest world.
            World world = getWorld();

            // Sprites:
            CMenu menu = new CMenu("mymenu", 10.0f, 10.0f, 100.0f, 200.0f);

            // Entities:
            TestPlayer player = new TestPlayer();

            // Add sprites to world.
            world.addDrawable(menu);

            // Add entities to world.
            world.loadAnimatedTextureForSprite(player);
            world.addSprite(player);

            // Setup sprites.
            populateMenu(menu);

            // Setup entities.
            player.setX(0.0f);
            player.setY(64.0f);
            player.select();
        }

        private void populateMenu(CMenu menu)
        {
            menu.addItem("Brightness",
                    new MenuItem(9,
                            new String[]
                                    {"-0.9",
                                            "-0.8",
                                            "-0.7",
                                            "-0.6",
                                            "-0.5",
                                            "-0.4",
                                            "-0.3",
                                            "-0.2",
                                            "-0.1",
                                            "0.0",
                                            "+0.1",
                                            "+0.2",
                                            "+0.3",
                                            "+0.4",
                                            "+0.5",
                                            "+0.6",
                                            "+0.7",
                                            "+0.8",
                                            "+0.9",
                                    }
                    )
            );
            menu.addItem("Contrast",
                    new MenuItem(9,
                            new String[]
                                    {
                                            "-0.9",
                                            "-0.8",
                                            "-0.7",
                                            "-0.6",
                                            "-0.5",
                                            "-0.4",
                                            "-0.3",
                                            "-0.2",
                                            "-0.1",
                                            "1.0",
                                            "+1.1",
                                            "+1.2",
                                            "+1.3",
                                            "+1.4",
                                            "+1.5",
                                            "+1.6",
                                            "+1.7",
                                            "+1.8",
                                            "+1.9",
                                    }
                    )
            );
            menu.addItem("Saturation",
                    new MenuItem(9,
                            new String[]
                                    {"-0.9",
                                            "-0.8",
                                            "-0.7",
                                            "-0.6",
                                            "-0.5",
                                            "-0.4",
                                            "-0.3",
                                            "-0.2",
                                            "-0.1",
                                            "0.0",
                                            "+0.1",
                                            "+0.2",
                                            "+0.3",
                                            "+0.4",
                                            "+0.5",
                                            "+0.6",
                                            "+0.7",
                                            "+0.8",
                                            "+0.9",
                                    }
                    )
            );
        }
    }
}
