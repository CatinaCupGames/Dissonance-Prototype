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

public class COval extends CShape
{
	// TODO: At this point I have not mapped the textures correctly.
	protected COval(String name, float x, float y, float w, float h, Texture texture, float sides)
	{
		super(name, x, y, w, h, texture);

		if(sides < 3.0f) throw new RuntimeException("The number of sides for: " + name + " must be greater than 2.0f.");

		this.sides = (float)Math.PI / (sides / 2.0f);
	}

	public COval(String name, float x, float y, float w, float h, Color color, float sides) throws Exception
	{
		super(name, x, y, w, h, color);

		if(sides < 3.0f) throw new RuntimeException("The number of sides for: " + name + " must be greater than 2.0f.");

		this.sides = (float)Math.PI / (sides / 2.0f);
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

		// TODO: Make local.
		float theta;

		glLoadIdentity();
		{
			glTranslatef(x + (w / 2), y + (h / 2), 0.0f);

			texture.bind();

			glBegin(GL_TRIANGLE_FAN); //LINE_LOOP for lines.
			{
				for(theta = 0.0f; theta < (float)Math.PI * 2.0f; theta += sides)
				{
					glVertex2f((float) (w / 2 * Math.cos(theta)), (float) (h / 2 * Math.sin(theta)));
				}
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

	private float sides;
}
