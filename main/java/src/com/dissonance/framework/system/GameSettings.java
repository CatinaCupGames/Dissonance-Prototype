package com.dissonance.framework.system;

import com.dissonance.framework.system.settings.Color;
import com.dissonance.framework.system.settings.Resolution;

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

        public static int window_width;
        public static int window_height;

        public static Resolution resolution;
        private static int game_width;
        private static int game_height;

        public static boolean fullscreen;

        static
        {
            window_width = 1366;
            window_height = 768;

            game_width = 1280;
            game_height = 720;
            resolution = new Resolution(game_width, game_height);

            fullscreen = true;
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
