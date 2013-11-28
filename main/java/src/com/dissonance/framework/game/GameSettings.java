package com.dissonance.framework.game;

import com.dissonance.framework.game.settings.Color;
import com.dissonance.framework.game.settings.Resolution;

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
            window_width = 1920;
            window_height = 1080;

            game_width = 1920;
            game_height = 1080;
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
