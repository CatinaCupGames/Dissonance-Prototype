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

        public static Color color;

        static
        {
            window_width = 1280;
            window_height = 720;

            game_width = 1280;
            game_height = 720;
            resolution = new Resolution(game_width, game_height);

            fullscreen = false;

            color = new Color(50, 50, 50, 50, 50, 50);
        }
    }

    public static class Graphics
    {
        private Graphics()
        {
        }
    }
}
