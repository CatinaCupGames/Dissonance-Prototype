package com.dissonance.framework.game.settings;

public class Color
{
    private Color()
    {
    }

    public Color(int brightness, int contrast, int saturation, int red, int green, int blue)
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
