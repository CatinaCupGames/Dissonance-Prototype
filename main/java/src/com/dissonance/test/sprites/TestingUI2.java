package com.dissonance.test.sprites;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;

import static org.lwjgl.opengl.GL11.*;

public class TestingUI2 extends AbstractUI {
    public TestingUI2(AbstractUI parent) {
        super(parent);
    }

    @Override
    protected void onOpen() {
        setWidth(32);
        setHeight(32);
        centerVertical();
        centerHorizontal();
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() { }

    @Override
    public void render() {
        float bx = width / 2f, by = height / 2f;
        float z = 0;
        float x = getX(), y = getY();
        glColor3f(0f, 1f, 0f);
        glBegin(GL_QUADS);
        glVertex3f(x - bx, y - by, z);
        glVertex3f(x + bx, y - by, z);
        glVertex3f(x + bx, y + by, z);
        glVertex3f(x - bx, y + by, z);
        glEnd();
    }
}
