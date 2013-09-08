package org.oyasunadev.li.liui.core;

import org.oyasunadev.li.liui.util.EScene;

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
	public void initialize()
	{
		if(scene != EScene.INITIALIZE)
		{
			scene = EScene.INITIALIZE;
		}
	}

	@Override
	public void preRender()
	{
		if(scene != EScene.PRERENDER)
		{
			scene = EScene.PRERENDER;
		}
	}

	@Override
	public void render(int delta)
	{
		if(scene != EScene.RENDER)
		{
			scene = EScene.RENDER;
		}

		frame++;
	}

	@Override
	public void postRender()
	{
		if(scene != EScene.POSTRENDER)
		{
			scene = EScene.POSTRENDER;
		}
	}

	@Override
	public void trash()
	{
		if(scene != EScene.TRASH)
		{
			scene = EScene.TRASH;
		}
	}

	public float x;
	public float y;
	public float w;
	public float h;
}
