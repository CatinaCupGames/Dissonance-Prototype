package com.dissonance.game.menus;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GameMenu extends UIElement {
    public GameMenu(String name) {
        super(name);
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
        setWidth(GameSettings.Display.window_width);
        setHeight(GameSettings.Display.window_height);
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
