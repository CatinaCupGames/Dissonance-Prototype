package org.oyasunadev.li.liui.core;

import com.dissonance.framework.render.Drawable;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 2:02 PM
 */

public abstract class CObject implements Drawable
{
	private CObject()
	{
		ID = 0;
		name = "UNKNOWN COBJECT WITH ID: " + ID + ".";
	}

	public CObject(String name)
	{
		ID = CObject.objects++;
		this.name = name;
	}

	public static int objects = 0;

	public final int ID;
	public final String name;

	public int frame;


    @Override
    public abstract void init();
    @Override
    public abstract void update();
    @Override
    public abstract void render();

    @Override
    public int compareTo(Drawable o)
    {
        return 0;
    }
}
