package org.oyasunadev.li.liui.component.shape;

import com.dissonance.framework.render.texture.Texture;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 5:59 PM
 */

public class CCircle extends COval
{
	public CCircle(String name, float x, float y, float s, Texture texture, float sides)
	{
		super(name, x, y, s, s, texture, sides);
	}

	public CCircle(String name, float x, float y, float s, Color color, float sides) throws Exception
	{
		super(name, x, y, s, s, color, sides);
	}
}
