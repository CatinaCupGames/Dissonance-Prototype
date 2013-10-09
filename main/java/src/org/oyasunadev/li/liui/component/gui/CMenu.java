package org.oyasunadev.li.liui.component.gui;

import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.render.Camera;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 10/6/13
 * Time: 10:36 PM
 */

public class CMenu extends UIElement
{
    public CMenu(String name, float x, float y, float w, float h)
    {
        super(name);

        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(Color.GREEN);
        g.drawRect((int)getX(), (int)getY(), getWidth() - 11, getHeight() - 11);

        g.setColor(Color.BLACK);
        g.fillRect((int)getX() + 1, (int)getY() + 1, getWidth() - 11 - 1, getHeight() - 11 - 1);

        float width = (float)g.getFont().getStringBounds("Realtime Settings", new FontRenderContext(new AffineTransform(), true, true)).getWidth();
        float height = (float)g.getFont().getStringBounds("Realtime Settings", new FontRenderContext(new AffineTransform(), true, true)).getHeight();

        g.setColor(Color.GREEN);
        g.drawLine((int)getX(), (int)getY() + (int)height + 2, (int)getX() + getWidth(), (int)getY() + (int)height + 2);

        g.setColor(Color.WHITE);
        g.drawString("Realtime Settings", getX() + 3.0f, getY() + height - 2.0f);

        String value;
        for(int i = 0; i < items.size(); i++)
        {
            value = items.values().toArray(new MenuItem[items.size()])[i].options[items.values().toArray(new MenuItem[items.size()])[i].i];
            width = (float)g.getFont().getStringBounds(value, new FontRenderContext(new AffineTransform(), true, true)).getWidth();

            g.drawString(items.keySet().toArray(new String[items.size()])[i], getX() + 3.0f, ((getY() + height - 3.0f) * 2.0f) + (12 * i) - 5.0f);
            g.drawString(
                    value,
                    getX() + getWidth() + 2.0f - (width * 2.0f), ((getY() + height - 3.0f) * 2.0f) + (12 * i) - 5.0f
            );
        }
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
        if(cx != Camera.getX() || cy != Camera.getY())
        {
            Vec2 pos = new Vec2(getWidth(), getHeight() / 1.5f);
            pos = new Vec2(getWidth() * 4.55f, getHeight() - 18.0f);
            pos = Camera.translateToScreenCord(pos);

            setX(pos.x);
            setY(pos.y);

            cx = Camera.getX();
            cy = Camera.getY();
        }
    }

    private final Map<String, MenuItem> items = new HashMap<String, MenuItem>();

    private float cx;
    private float cy;

    public void addItem(String key, MenuItem item)
    {
        items.put(key, item);
    }
}
