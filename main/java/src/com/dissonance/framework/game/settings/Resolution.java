package com.dissonance.framework.game.settings;

public class Resolution
{
    private Resolution()
    {
    }

    public Resolution(double width, double height)
    {
        this.width = width;
        this.height = height;

        aspectRatio = new AspectRatio(this.width, this.height);
    }

    private double width;
    private double height;

    public AspectRatio aspectRatio;

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
