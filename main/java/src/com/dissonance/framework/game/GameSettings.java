package com.dissonance.framework.game;

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

        public static class Resolution
        {
            private Resolution()
            {
            }

            private Resolution(double width, double height)
            {
                this.width = width;
                this.height = height;

                aspectRatio = new AspectRatio(this.width, this.height);
            }

            private double width;
            private double height;

            public AspectRatio aspectRatio;

            public class AspectRatio
            {
                private AspectRatio()
                {
                }

                private AspectRatio(double width, double height)
                {
                    calculate(width, height);
                }

                @Override
                public String toString()
                {
                    StringBuffer buffer = new StringBuffer();

                    buffer.append(arWidth);
                    buffer.append("x");
                    buffer.append(arHeight);

                    return buffer.toString();
                }

                public double arWidth;
                public double arHeight;

                private void calculate(double width, double height)
                {
                    double remainder;
                    double newWidth = width;
                    double newHeight = height;

                    while(height != 0)
                    {
                        remainder = width % height;
                        width = height;
                        height = remainder;
                    }

                    newWidth = newWidth / width;
                    newHeight = newHeight / width;

                    arWidth = newWidth;
                    arHeight = newHeight;
                }

                @Deprecated
                public double getAspectRatioWidth()
                {
                    return arWidth;
                }

                @Deprecated
                public double getAspectRatioHeight()
                {
                    return arHeight;
                }

                public double[] getAspectRatio()
                {
                    return new double[] {arWidth, arHeight};
                }
            }

            public double getWidth()
            {
                return width;
            }

            public void setWidth(double width)
            {
                this.width = width;

                aspectRatio = new AspectRatio(this.width, this.height);
            }

            public double getHeight()
            {
                return height;
            }

            public void setHeight(double height)
            {
                this.height = height;

                aspectRatio = new AspectRatio(this.width, this.height);
            }
        }

        public static class Color
        {
            private Color()
            {
            }

            private Color(int brightness, int contrast, int saturation, int red, int green, int blue)
            {
                this.brightness = brightness;
                this.contrast = contrast;
                this.saturation = saturation;
                this.red = red;
                this.green = green;
                this.blue = blue;
            }

            public int brightness;
            public int contrast;
            public int saturation;
            public int red;
            public int green;
            public int blue;
        }
    }

    public static class Graphics
    {
        private Graphics()
        {
        }
    }
}
