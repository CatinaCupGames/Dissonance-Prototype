package com.dissonance.framework.system;

import com.dissonance.framework.system.settings.Color;
import com.dissonance.framework.system.settings.Resolution;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class GameSettings {

    public static class Audio {
        private Audio() {
        }
    }

    public static class Display {
        private Display() {
        }

        /**
         * The default font used by the game. <br></br>
         * <b>Must restart game for value changes to take effect</b>
         */
        public static Font GAME_FONT;

        /**
         * The width of the screen window, in pixels. <br></br>
         * <b>Must restart game for value changes to take effect</b>
         */
        public static int window_width;
        /**
         * The height of the screen window, in pixels. <br></br>
         * <b>Must restart game for value changes to take effect</b>
         */
        public static int window_height;

        /**
         * The {@link com.dissonance.framework.system.settings.Resolution} of the game window. <br></br>
         * <b>Must restart game for value changes to take effect</b>
         */
        public static Resolution resolution;
        private static int game_width;
        private static int game_height;

        /**
         * Whether the game is being played in fullscreen mode or not. <br></br>
         * <b>Must restart game for value changes to take effect</b>
         */
        public static boolean fullscreen;

        static {
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

    public static class Graphics {
        /**
         * The maximum FPS the {@link com.dissonance.framework.render.RenderService} can exceeded. If the value is -1, then
         * then there will be no FPS limit.
         */
        public static int FPSLimit;

        private Graphics() {
        }

        public static Color color;

        static {
            FPSLimit = -1;
            color = new Color(0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
}
