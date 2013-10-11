package com.dissonance.framework.render;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.game.input.InputService;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.utils.Validator;
import com.dissonance.game.Main;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.oyasunadev.li.liui.component.shape.CSquare;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluErrorString;

public class RenderService extends Service {
    public static final int WORLD_DATA_TYPE = 0;
    public static RenderService INSTANCE;

    /****************************************************
     * RENDER SETTINGS                                  *
     ***************************************************/
    /*public static int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    public static double GAME_WIDTH = (double)WINDOW_WIDTH, GAME_HEIGHT = (double)WINDOW_HEIGHT; // TODO: For now the resolution will be the same as the window size;
    public static boolean fullscreen = true; //TODO Create config to change this value*/

    private CSquare square;

    public static float TIME_DELTA;
    public static long RENDER_THREAD_ID;
    private World current_world;
    private boolean drawing;
    private boolean looping;
    private float fpsCount;
    private float fpsTime;
    private float rotx, roty;
    private float posx, posy;

    long cur = System.currentTimeMillis();
    long now;

    public static boolean isInRenderThread() {
        return Thread.currentThread().getId() == RENDER_THREAD_ID;
    }

    /**
     * Set the display mode to be used
     *
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    private void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }

    @Override
    protected void onStart() {

        drawing = true;
        INSTANCE = this;

        RENDER_THREAD_ID = Thread.currentThread().getId();

        try {
            setDisplayMode(GameSettings.Display.window_width, GameSettings.Display.window_height, GameSettings.Display.fullscreen);
            Display.create();
            AL.create();

            glClearColor(0f, 0f, 0f, 1f);
            glClearDepth(1f);
            glViewport(0, 0, GameSettings.Display.window_width, GameSettings.Display.window_height); // TODO: Not sure if it should be WINDOW_ or GAME_.
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0.0f, GameSettings.Display.resolution.getWidth(), GameSettings.Display.resolution.getHeight(), 0.0f, 0f, -1f);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            //gluPerspective(90, Game.WINDOW_WIDTH / Game.WINDOW_HEIGHT, 0.1f, 10000);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glLoadIdentity();

            System.out.println("OpenGL version: " + glGetString(GL_VERSION));

            System.out.println("Building shaders..");
            long ms = System.currentTimeMillis();
            ShaderFactory.buildAllShaders();
            System.out.println("Done! Took " + (System.currentTimeMillis() - ms) + "ms.");

        } catch (LWJGLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (looping) {
                looping = false;
                Display.destroy();
            }
        }

        ServiceManager.createService(InputService.class);
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onResume() {
    }

    @Override
    protected void onTerminated() {
        drawing = false;
        GameService.getSoundSystem().unloadAllSounds();
        current_world = null;
        INSTANCE = null;
        Display.destroy();
        //Kill any waiting threads
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.WAITING && !t.getName().contains("Disposer")) {
                t.interrupt();
            }
        }
    }

    @Override
    public void provideData(Object obj, int type) {
        Validator.validateNotNull(obj, "object");
        if (type == WORLD_DATA_TYPE) {
            Validator.validateClass(obj, World.class);

            this.current_world = (World) obj;
        }
    }

    @Override
    protected String getName() {
        return "RenderService Thread";
    }

    @Override
    public void onUpdate() {
        now = System.currentTimeMillis();
        TIME_DELTA = ((now - cur) / 100.0f);
        if (current_world != null && !isPaused()) {
            boolean close = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE); //TODO REMOVE
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(1f, 1f, 1f, 1f);
            glMatrixMode(GL_MODELVIEW);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glLoadIdentity();
            glScalef(2f, 2f, 1f);
            glTranslatef(-Camera.getX(), -Camera.getY(), 0f);

            Iterator<UpdatableDrawable> updates = current_world.getUpdatables();
            while (updates.hasNext()) {
                UpdatableDrawable s = updates.next();
                if (s == null)
                    continue;
                try {
                    s.update();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            ShaderFactory.executePreRender();

            Iterator<Drawable> sprites = current_world.getSortedDrawables();
            while (sprites.hasNext()) {
                Drawable s = sprites.next();
                if (s == null)
                    continue;
                try {
                    if (s instanceof Sprite) {
                        if (Camera.isOffScreen((Sprite)s, 2))
                            continue;
                    } else if (Camera.isOffScreen(s.getX(), s.getY(), 16, 16, 2)) //Assume everything is 32x32
                        continue;
                    s.render();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            try {
                AnimationFactory.executeTick(); //Execute any animation
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            Camera.executeEase(); //Execute any interlop

            ShaderFactory.executePreRender();

            exitOnGLError("RenderService.renderSprites");

            Display.update();
            fpsTime += TIME_DELTA;
            fpsCount++;
            if (fpsCount == 100) {
                fpsCount = 0;
                Display.setTitle("FPS: " + (1000f/fpsTime));
                fpsTime = 0;
            }
            cur = now;
            if (Display.isCloseRequested() || close) {
                GameService.handleKillRequest();
                ServiceManager.getService(InputService.class).terminate();
                terminate();
                Main.getSystemTicker().stopTick();
                return;
            }
        } else {
            try {
                Thread.sleep(15); //Keep the thread busy
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        updateInput();
    }

    private void updateInput() {
        int multi = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 2 : 1;

        /*if (Keyboard.isKeyDown(Keyboard.KEY_W))
            Camera.setY(Camera.getY() + (1.5f * multi));
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            Camera.setY(Camera.getY() - (1.5f * multi));
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            Camera.setX(Camera.getX() + (1.5f * multi));
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            Camera.setX(Camera.getX() - (1.5f * multi));*/
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