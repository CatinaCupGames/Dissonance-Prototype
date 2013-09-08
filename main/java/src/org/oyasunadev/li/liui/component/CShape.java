package org.oyasunadev.li.liui.component;

import com.dissonance.framework.render.texture.Texture;
import org.oyasunadev.li.liui.core.CComponent;
import org.oyasunadev.li.liui.util.TextureUtils;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 6:10 PM
 */

public class CShape extends CComponent
{
	public CShape(String name, float x, float y, float w, float h, Texture texture)
	{
		super(name, x, y, w, h);

		this.texture = texture;
	}

	public CShape(String name, float x, float y, float w, float h, Color color) throws Exception
	{
		this(name, x, y, w, h, TextureUtils.getTextureFromColor(color));
	}

	@Override
	public void initialize()
	{
		super.initialize();
	}

	@Override
	public void preRender()
	{
		super.preRender();
	}

	@Override
	public void render(int delta)
	{
		super.render(delta);
	}

	@Override
	public void postRender()
	{
		super.postRender();
	}

	@Override
	public void trash()
	{
		super.trash();
	}

	public Texture texture;
}
