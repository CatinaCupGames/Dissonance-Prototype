package com.dissonance.test.sprites;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;

import static org.lwjgl.opengl.GL11.*;

public class TestingUI extends AbstractUI {
    @Override
    protected void onOpen() {
        setWidth(64);
        setHeight(64);
        marginLeft(0f);
        centerVertical();
    }

    @Override
    protected void onClose() { }

    float xval;
    @Override
    public void update() {
        xval++;
        marginLeft(xval);
    }

    @Override
    public void onRender() {
        float bx = width / 2f, by = height / 2f;
        float z = 0;
        float x = getX(), y = getY();
        glColor3f(0f, 0f, 1f);
        glBegin(GL_QUADS);
        glVertex3f(x - bx, y - by, z);
        glVertex3f(x + bx, y - by, z);
        glVertex3f(x + bx, y + by, z);
        glVertex3f(x - bx, y + by, z);
        glEnd();
    }
}
