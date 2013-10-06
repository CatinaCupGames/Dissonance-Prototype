package com.dissonance.framework.game.settings;

public class Color
{
    private Color()
    {
    }

    public Color(float brightness, float contrast, float saturation, float red, float green, float blue)
    {
        this.brightness = brightness;
        this.contrast = contrast;
        this.saturation = saturation;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float brightness;
    public float contrast;
    public float saturation;
    public float red;
    public float green;
    public float blue;
}
