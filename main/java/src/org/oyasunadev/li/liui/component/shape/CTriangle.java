package org.oyasunadev.li.liui.component.shape;

import com.dissonance.framework.render.texture.Texture;
import org.oyasunadev.li.liui.component.CShape;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 2:29 PM
 */

public class CTriangle extends CShape
{
	public CTriangle(String name, float x, float y, float w, float h, Texture texture)
	{
		super(name, x, y, w, h, texture);
	}

	public CTriangle(String name, float x, float y, float w, float h, Color color) throws Exception
	{
		super(name, x, y, w, h, color);
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

		glLoadIdentity();
		{
			texture.bind();

			glBegin(GL_TRIANGLES);
			{
				// Top
				glTexCoord2f(0.5f, 0.5f);
				glVertex2f(x + (w / 2), y);

				// Right
				glTexCoord2f(1, 1);
				glVertex2f(x + w, y + h);

				// Left
				glTexCoord2f(0, 1);
				glVertex2f(x, y + h);
			}
			glEnd();
		}
		glLoadIdentity();
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
}
