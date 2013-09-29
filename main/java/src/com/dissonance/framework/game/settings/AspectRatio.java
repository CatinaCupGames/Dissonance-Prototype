package com.dissonance.framework.game.settings;

public class AspectRatio
{
    private AspectRatio()
    {
    }

    public AspectRatio(double width, double height)
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
