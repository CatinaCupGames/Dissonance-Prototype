package com.dissonance.game.sprites.menu.loading;

import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.*;

public class Spinner extends ImageSprite {
    private static final long WAIT_TIME = 2000;
    private static final long ANIMATE_TIME = 1800;

    private float angle;
    private boolean animate;
    private boolean wait;
    private float start;
    private float end;
    private long startTime;

    public Spinner() {
        super("sprites/img/icon_128.png");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setWidth(64);
        setHeight(64);
    }

    @Override
    public void render() {
        if (getTexture() == null)
            return;
        update(); //I'm a dirty little cheater

        float bx = getWidth() / 2f;
        float by = getHeight() / 2f;
        float z = 0f;

        getTexture().bind();
        glPushMatrix();
        glTranslatef(x, y, z);
        glRotatef(angle, 0, 0, 1f);

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(-bx, -by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(bx, -by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(bx, by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(-bx, by, z);
        glEnd();
        getTexture().unbind();

        glPopMatrix();
    }

    private void update() {
        if (!wait && !animate) {
            animate = true;
            startTime = System.currentTimeMillis();
            start = angle;
            end = angle - 180;
        } else if (animate) {
            angle = Camera.ease(start, end, ANIMATE_TIME, (System.currentTimeMillis() - startTime));
            if (angle == end) {
                animate = false;
                wait = true;

                startTime = System.currentTimeMillis();
            }
        } else {
            if (System.currentTimeMillis() - startTime > WAIT_TIME) {
                wait = false;
            }
        }
    }
}
