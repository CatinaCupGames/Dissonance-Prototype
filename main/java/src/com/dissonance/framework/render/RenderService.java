package com.dissonance.framework.render;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.game.input.InputService;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GLContext;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluErrorString;

public class RenderService extends Service {
    public static final int WORLD_DATA_TYPE = 0;
    public static final int ENABLE_CROSS_FADE = 1;
    public static final int CROSS_FADE_DURATION = 2;
    public static RenderService INSTANCE;

    /****************************************************
     * RENDER SETTINGS                                  *
     ***************************************************/
    /*public static int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    public static double GAME_WIDTH = (double)WINDOW_WIDTH, GAME_HEIGHT = (double)WINDOW_HEIGHT; // TODO: For now the resolution will be the same as the window size;
    public static boolean fullscreen = true; //TODO Create config to change this value*/

    public static float TIME_DELTA;
    public static long RENDER_THREAD_ID;
    private World current_world;
    private boolean looping;
    private float fpsCount;
    private float fpsTime;
    private boolean crossfade;
    private World next_world;
    private ContextCapabilities capabilities;

    private long startTime;
    private boolean isFading;
    private float speed;
    private static float curAlpha;
    private float newAlpha;
    private float startAlpha;

    long next_tick;
    long cur = System.currentTimeMillis();
    long now;
    long started;

    public static boolean isInRenderThread() {
        return Thread.currentThread().getId() == RENDER_THREAD_ID;
    }

    public void fadeToBlack(float speed) {
        fadeToAlpha(speed, 0f);
    }

    public void fadeFromBlack(int speed) {
        fadeToAlpha(speed, 1f);
    }

    public void fadeToAlpha(float speed, float alpha) {
        isFading = true;
        newAlpha = alpha;
        startAlpha = curAlpha;
        this.speed = speed;
        startTime = System.currentTimeMillis();
    }

    public boolean isCrossFading() {
        return crossfade;
    }

    public static float getCurrentAlphaValue() {
        return curAlpha;
    }

    public void waitForFade() throws InterruptedException {
        if (current_world == null)
            return;
        while (isFading) {
            Thread.sleep((long) speed);
        }
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

                        // if we've found a match for bpp and frequency against the
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
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    @Override
    protected void onStart() {
        INSTANCE = this;

        RENDER_THREAD_ID = Thread.currentThread().getId();

        try {
            setDisplayMode(GameSettings.Display.window_width, GameSettings.Display.window_height, GameSettings.Display.fullscreen);
            Display.create();
		//ROBO //todo get all this changed to proper OGL
            glClearColor(0f, 0f, 0f, 1f);
            glClearDepth(1f);
            glViewport(0, 0, GameSettings.Display.window_width, GameSettings.Display.window_height);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0.0f, GameSettings.Display.resolution.getWidth(), GameSettings.Display.resolution.getHeight(), 0.0f, 0f, -1f);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,  GL_MODULATE);
            glLoadIdentity();
            curAlpha = 1f;
            capabilities = GLContext.getCapabilities();

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
        started = System.currentTimeMillis();
        next_tick = getTimeSinceStartMillis();
    }

    private long getTimeSinceStartMillis() {
        return System.currentTimeMillis() - started;
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onResume() {
    }

    @Override
    protected void onTerminated() {
        //GameService.getSoundSystem().unloadAllSounds();
        current_world = null;
        INSTANCE = null;
        Display.destroy();
    }

    private static void killAll() {
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

            if (!crossfade)
                this.current_world = (World) obj;
            else {
                this.next_world = (World) obj;
                fadeStartTime = System.currentTimeMillis();
            }
        } else if (type == ENABLE_CROSS_FADE) {
            this.crossfade = (boolean)obj;
        } else if (type == CROSS_FADE_DURATION && crossfade) {
            this.fadeDuration = (float)obj;
        }
    }

    @Override
    protected String getName() {
        return "RenderService Thread";
    }

    private long fadeStartTime;
    private float fadeDuration;
    @Override
    public void onUpdate() {
        cur = now;
        now = getTimeSinceStartMillis();
        TIME_DELTA = (now - cur) / 100.0f;
        if (current_world != null && !isPaused()) {
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
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0f, 0f, 0f, 1f);
		//ROBO //todo get this crap changed into proper ogl
            glMatrixMode(GL_MODELVIEW);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glLoadIdentity();
            glScalef(2f, 2f, 1f);
            glTranslatef(-Camera.getX(), -Camera.getY(), 0f);
	    //ROBO //todo change this crap into proper postprocess and per material shaders
            ShaderFactory.executePreRender();


            if (isFading) {
                long time = System.currentTimeMillis() - startTime;
                curAlpha = Camera.ease(startAlpha, newAlpha, speed, time);
                if (curAlpha == newAlpha)
                    isFading = false;
            }

            glColor4f(1f, 1f, 1f, curAlpha);
		//ROBO //todo get all these into proper batches
            Iterator<Drawable> sprites = current_world.getSortedDrawables();
            while (sprites.hasNext()) {
                Drawable s = sprites.next();
                if (s == null)
                    continue;
                try {
                    if (s instanceof Sprite) {
                        if (Camera.isOffScreen((Sprite)s, 2))
                            continue;
                    } else if (Camera.isOffScreen(s.getX(), s.getY(), s.getWidth() / 2, s.getHeight() / 2, 2)) //Assume everything is 32x32
                        continue;
                    s.render();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            if (next_world != null) {
                long time = System.currentTimeMillis() - fadeStartTime;
                float percent;
                if (time > fadeDuration) {
                    percent = 1;
                } else {
                    percent = time / fadeDuration;
                }
                float emu = curAlpha;
                curAlpha = percent; //Some drawables require the curAlpha to be set, so we save what it use to be and set it
                glColor4f(1f, 1f, 1f, curAlpha);

                Iterator<Drawable> next_sprites = next_world.getSortedDrawables();
                while (next_sprites.hasNext()) {
                    Drawable d = next_sprites.next();
                    if (d == null)
                        continue;
                    try {
                        if (d instanceof Sprite) {
                            if (Camera.isOffScreen((Sprite)d, 2))
                                continue;
                        } else if (Camera.isOffScreen(d.getX(), d.getY(), d.getWidth() / 2, d.getHeight() / 2, 2)) //Assume everything is 32x32
                            continue;
                        d.render();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

                curAlpha = emu;

                if (percent == 1) {
                    current_world = next_world;
                    next_world = null;
                }
            }

            try {
                AnimationFactory.executeTick(); //Execute any animation
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            Camera.executeAnimation(); //Execute any interlop

            ShaderFactory.executePostRender();
            exitOnGLError("RenderService.renderSprites");

            Display.update();

            fpsTime += TIME_DELTA;
            fpsCount++;
            if (fpsCount == 100) {
                fpsCount = 0;
                Display.setTitle("FPS: " + (1000f/fpsTime));
                fpsTime = 0;
            }
            if (Display.isCloseRequested()) {
                kill();
            }
        } else {
            try {
                Thread.sleep(15); //Keep the thread busy
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public World getCurrentDrawingWorld() {
        return current_world;
    }

    public boolean isFading() {
        return isFading;
    }

    public static void kill() {
        GameService.handleKillRequest();
        Service s = ServiceManager.getService(InputService.class);
        if (s != null) s.terminate();
        Service s2 = ServiceManager.getService(RenderService.class);
        if (s2 != null) s2.terminate();
        killAll();
    }

    public static enum TransitionType {
        CROSSFADE,
        FADETOBLACK,
        NONE
    }
}