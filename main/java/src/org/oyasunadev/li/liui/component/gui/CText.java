package org.oyasunadev.li.liui.component.gui;

import com.dissonance.framework.game.sprites.UIElement;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 10/6/13
 * Time: 10:12 PM
 */

public class CText extends UIElement
{
    public CText(String name, float x, float y, String text)
    {
        super(name);

        setX(x);
        setY(y);

        this.text = text;
    }

    @Override
    public void draw(Graphics2D g)
    {
        //setWidth((float)g.getFont().getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth());
        //setHeight((float)g.getFont().getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getHeight());

        g.setColor(Color.WHITE);
        g.drawRect((int)getX(), (int)getY(), getWidth(), getHeight());
        g.setColor(Color.BLUE);
        g.drawString(text, 0, getHeight() / 2);
    }

    @Override
    public void init()
    {
        setWidth(999);
        setHeight(999);
    }

    @Override
    public void update()
    {
    }

    private String text;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
