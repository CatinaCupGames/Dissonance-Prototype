package org.oyasunadev.li.liui.component.shape;

import com.dissonance.framework.render.texture.Texture;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 5:03 PM
 */

public class CSquare extends CRectangle
{
	public CSquare(String name, float x, float y, float s, Texture texture)
	{
		super(name, x, y, s, s, texture);
	}

	public CSquare(String name, float x, float y, float s, Color color) throws Exception
	{
		super(name, x, y, s, s, color);
	}
}
