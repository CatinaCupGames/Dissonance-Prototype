package com.tog.framework.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;


import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.game.world.World;
import com.tog.framework.system.Game;
import com.tog.framework.system.Service;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.Iterator;

public class RenderService implements Service {
    public static final int WORLD_DATA_TYPE = 0;
    private ArrayList<Runnable> toRun = new ArrayList<>();
    private Thread service_thread;
    private World current_world;
    private boolean drawing;
    private boolean paused;

    private float rotx, roty, rotz;
    private float posx, posy, posz = 30f;
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
    public void runOnServiceThread(Runnable runnable) {
        synchronized (toRun) {
            toRun.add(runnable);
        }
    }

    @Override
    public boolean isPaused() {
        return paused;
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

            gluPerspective(90, Game.GAME_WIDTH/Game.GAME_HEIGHT, 0.1f, 10000);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glLoadIdentity();

            while (drawing) {
                Iterator<Runnable> runs = toRun.iterator();
                while (runs.hasNext()) {
                    Runnable r = runs.next();
                    try {
                        r.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    runs.remove();
                }
                //TEMP CODE BEGIN
                while (Keyboard.next()) {
                    if (!Keyboard.getEventKeyState()) continue;

                    switch (Keyboard.getEventKey()) {
                        case Keyboard.KEY_E:
                            posz += 5;
                            break;
                        case Keyboard.KEY_D:
                            posz -= 5;
                            break;
                        case Keyboard.KEY_A:
                            roty += 5;
                            break;
                        case Keyboard.KEY_S:
                            roty -= 5;
                            break;
                        case Keyboard.KEY_Q:
                            rotx += 5;
                            break;
                        case Keyboard.KEY_W:
                            rotx -= 5;
                            break;
                        case Keyboard.KEY_Z:
                            rotz += 5;
                            break;
                        case Keyboard.KEY_X:
                            rotz -= 5;
                            break;
                    }
                }
                //END TEMP CODE
                if (current_world != null && !paused) {
                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                    glClearColor(0f, 0f, 0f, 1f);
                    glMatrixMode(GL_MODELVIEW);
                    glLoadIdentity();

                    glRotatef(-rotx, 1, 0, 0);
                    glRotatef(-roty, 0, 1, 0);
                    glRotatef(-rotz, 0, 0, 1);
                    glTranslatef(-posx, -posy, -posz);

                    final Iterator<Sprite> sprites = current_world.getSprites();
                    while (sprites.hasNext()) {
                        Sprite s = sprites.next();
                        if (s.getTexture() == null)
                            continue;
                        s.getTexture().bind();
                        float cx = s.getTexture().getWidth();
                        float cy = s.getTexture().getHeight();
                        float bx = s.getTexture().getImageWidth();
                        float by = s.getTexture().getImageHeight();
                        glColor3f(1f, 1f, 1f);
                        glBegin(GL_QUADS);
                        glTexCoord3f(s.getX() + cx, s.getY(), 0f); glVertex3f(s.getX() - bx, s.getY() + by, 0f);
                        glTexCoord3f(s.getX(), s.getY(), 0f); glVertex3f(s.getX() + bx, s.getY() + by, 0f);
                        glTexCoord3f(s.getX(), s.getY() + cy, 0f); glVertex3f(s.getX() + bx, s.getY() - by, 0f);
                        glTexCoord3f(s.getX() + cx, s.getY() + cy, 0f); glVertex3f(s.getX() - bx, s.getY() - by, 0f);
                        glEnd();
                    }

                    exitOnGLError("RenderService.renderSprites");

                    Display.update();
                } else {
                    try {
                        Thread.sleep(15); //Keep the thread busy
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Display.destroy();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private void exitOnGLError(String errorMessage) {
        int errorValue = glGetError();

        if (errorValue != GL_NO_ERROR) {
            String errorString = gluErrorString(errorValue);
            System.err.println("ERROR AT: " + errorMessage + " - " + errorString);

            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}
