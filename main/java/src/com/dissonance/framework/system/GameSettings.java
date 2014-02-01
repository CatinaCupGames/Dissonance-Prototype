package com.dissonance.framework.system;

import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.system.settings.Color;
import com.dissonance.framework.system.settings.Resolution;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GameSettings
{
    /**
     * Notes:
     *  - Remove the private constructors if you want.
     */

    public static class Controls
    {
        private Controls()
        {
        }
    }

    public static class Audio
    {
        private Audio()
        {
        }
    }

    public static class Display
    {
        private Display()
        {
        }

        public static Font GAME_FONT;

        public static int window_width;
        public static int window_height;

        public static Resolution resolution;
        private static int game_width;
        private static int game_height;

        public static boolean fullscreen;

        static
        {
            window_width = 1280;
            window_height = 720;

            game_width = 1280;
            game_height = 720;
            resolution = new Resolution(game_width, game_height);

            fullscreen = false;
            InputStream in = GameSettings.class.getClassLoader().getResourceAsStream("fonts/INFO56_0.ttf");
            if (in != null) {
                try {
                    GAME_FONT = Font.createFont(Font.TRUETYPE_FONT, in);
                } catch (FontFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Graphics
    {
        private Graphics()
        {
        }

        public static Color color;

        static
        {
            color = new Color(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
