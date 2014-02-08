package com.dissonance.game.menus;

import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.game.sprites.ui.impl.UIElement;
import com.dissonance.framework.render.Camera;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class GameMenu extends UIElement {
    public GameMenu() {
        super();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillRect(0, 0, (int)getWidth(), (int)getHeight());

        graphics2D.setColor(Color.BLACK);
        String text = "Wow, such menu";
        graphics2D.drawString(text, (getWidth() / 2) - (graphics2D.getFontMetrics().stringWidth(text) / 2), graphics2D.getFontMetrics().getHeight() + 400);
        String button1 = "Button 1";
        graphics2D.drawString(button1, (getWidth() / 2) - (graphics2D.getFontMetrics().stringWidth(button1)), graphics2D.getFontMetrics().getHeight() + 500);
    }

    @Override
    public void init() {
        setWidth(GameSettings.Display.window_width / 2);
        setHeight(GameSettings.Display.window_height / 2);
        Vector2f pos = new Vector2f(GameSettings.Display.window_width / 4 , GameSettings.Display.window_height / 4);
        pos = Camera.translateToScreenCord(pos);
        System.out.println(pos);
        setX(pos.x);
        setY(pos.y);
    }

    @Override
    public void update() {

    }
}
