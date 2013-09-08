package org.oyasunadev.li.liui.core;

import org.oyasunadev.li.liui.util.EScene;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 2:02 PM
 */

public abstract class CObject
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

	public EScene scene;

	public int frame;

	public abstract void initialize();
	public abstract void preRender();
	public abstract void render(int delta);
	public abstract void postRender();
	public abstract void trash();
}
