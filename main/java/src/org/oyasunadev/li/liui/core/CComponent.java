package org.oyasunadev.li.liui.core;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 2:15 PM
 */

public class CComponent extends CObject
{
    public CComponent(String name, float x, float y, float w, float h)
    {
        super(name);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
    }

    @Override
    public void render()
    {
        frame++;
    }

    @Override
    public float getX() {
        return y;
    }

    @Override
    public float getY() {
        return x;
    }

    public float x;
    public float y;
    public float w;
    public float h;
}
