package org.oyasunadev.li.liui.component.gui;

import com.dissonance.framework.render.texture.Texture;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.oyasunadev.li.liui.component.shape.CRectangle;
import org.oyasunadev.li.liui.core.CComponent;
import org.oyasunadev.li.liui.util.TextureUtils;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 9/1/13
 * Time: 7:05 PM
 */

public class CPanel extends CComponent
{
	public CPanel(String name, float x, float y, float w, float h, Texture toolbarTexture, Texture contentTexture)
	{
		super(name, x, y, w, h);

		toolbar = new CRectangle(name + "->toolbar", x, y, w, 20, toolbarTexture);
		content = new CRectangle(name + "->content", x, y + 20, w, h, contentTexture);
	}

	public CPanel(String name, float x, float y, float w, float h, Color toolbarColor, Color contentColor) throws Exception
	{
		this(
				name,
				x, y,
				w, h,
                TextureUtils.getTextureFromColor(toolbarColor),
                TextureUtils.getTextureFromColor(contentColor)
		);
	}

	@Override
	public void init()
	{
		super.init();

		toolbar.init();
		content.init();
	}

	@Override
	public void update()
	{
		super.update();

		toolbar.update();
		content.update();
	}

	@Override
	public void render()
	{
		super.render();

		controlPanel();
		controlKeyboard();
		controlMouse();

		toolbar.render();
		content.render();
	}

	private CRectangle toolbar;
	private CRectangle content;

	private boolean moving;

	private int mouseX;
	private int mouseY;
	private boolean mouseLeft;
	private boolean mouseRight;

	private void controlPanel()
	{
		if(moving)
		{
			toolbar.x = mouseX - (toolbar.w / 2);
			toolbar.y = mouseY - (toolbar.h / 2);

			// TODO: Will this cause lag for the content due to the fact that I'm setting the position indirectly.? I suppose it's possible with many CPanels rendered.
			content.x = toolbar.x;
			content.y = toolbar.y + 20;
		}
	}

	private void controlKeyboard()
	{
	}

	private void controlMouse()
	{
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		mouseLeft = Mouse.isButtonDown(0);
		mouseRight = Mouse.isButtonDown(1);

		while(Mouse.next())
		{
			if(mouseLeft)
			{
				if(mouseX > toolbar.x && mouseX < toolbar.x + toolbar.w &&
						mouseY > toolbar.y && mouseY < toolbar.y + toolbar.h)
				{
					moving = true;
				}
			} else {
				moving = false;
			}
		}
	}
}
