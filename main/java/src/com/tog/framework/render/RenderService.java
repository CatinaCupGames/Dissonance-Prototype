package com.tog.framework.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;


import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.game.world.World;
import com.tog.framework.system.Game;
import com.tog.framework.system.Service;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Iterator;

public class RenderService implements Service {
    public static final int WORLD_DATA_TYPE = 0;

    private Thread service_thread;
    private World current_world;
    private boolean drawing;
    private boolean paused;
    @Override
    public Thread start() {
        service_thread = new Thread(this);
        drawing = true;
        service_thread.start();
        return service_thread;
    }

    @Override
    public void pause() {
        if (paused)
            throw new IllegalStateException("This service is already paused!");
        paused = true;
    }

    @Override
    public void resume() {
        if (!paused)
            throw new IllegalStateException("This service is not paused!");
        paused = false;
    }

    @Override
    public void terminate() {
        drawing = false;
        service_thread.interrupt();
        try {
            service_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service_thread = null;
        current_world = null;
    }

    @Override
    public void provideData(Object obj, int type) {
        Validator.validateNotNull(obj, "object");
        if (type == WORLD_DATA_TYPE) {
            Validator.validateClass(obj, World.class);

            this.current_world = (World)obj;
        }
    }

    @Override
    public void run() {
        try {
            Display.setDisplayMode(new DisplayMode(Game.GAME_WIDTH, Game.GAME_HEIGHT));
            Display.create();

            glClearColor(0f, 0f, 0f, 1f);
            glClearDepth(1f);
            glViewport(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            gluPerspective(90, Game.GAME_WIDTH/Game.GAME_HEIGHT, 0.1f, 100);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glLoadIdentity();

            while (drawing) {
                if (current_world != null && !paused) {
                    final Iterator<Sprite> sprites = current_world.getSprites();
                    while (sprites.hasNext()) {
                        Sprite s = sprites.next();
                        glTranslatef(s.getX(), s.getY(), 0f);
                        s.getTexture().bind();
                        float cx = s.getTexture().getCx();
                        float cy = s.getTexture().getCy();
                        glBegin(GL_QUADS);
                        glTexCoord2f(0f, 0f); glVertex3f(-cx, -cy, 0f);
                        glTexCoord2f(0f, cy); glVertex3f(-cx, cy, 0f);
                        glTexCoord2f(cx, cy); glVertex3f(cx, cy, 0f);
                        glTexCoord2f(cx, 0f); glVertex3f(cx, -cy, 0f);
                        glEnd();
                    }

                    Display.update();
                } else {
                    try {
                        Thread.sleep(15); //Keep the thread busy
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        } finally {
            Display.destroy();
        }
    }
}
