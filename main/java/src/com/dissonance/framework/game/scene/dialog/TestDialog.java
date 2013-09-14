package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.sprites.UIElement;

import java.awt.*;

public class TestDialog extends UIElement {

    private String text = "YES";
    public TestDialog(String name) {
        super(name);
    }

    public TestDialog() {
        this("test");
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(0, 0, getWidth(), getHeight());
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawString(text, 0, getHeight() / 2);
    }

    @Override
    public void init() {
        setWidth(100);
        setHeight(100);
    }

    int i = 0;
    @Override
    public void update() {
        i++;
        if (i % 15 == 0) {
            text = text.equals("YES") ? "NO" : "YES";
            completelyInvalidateView();
        }
    }
}
