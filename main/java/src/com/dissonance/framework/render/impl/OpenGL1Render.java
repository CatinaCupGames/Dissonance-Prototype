package com.dissonance.framework.render.impl;

import com.dissonance.framework.game.sprites.animation.AnimationFactory;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.*;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.util.glu.GLU.gluErrorString;

public class OpenGL1Render implements Renderer {
    public static long RENDER_THREAD_ID;
    private World current_world;
    private boolean looping;
    private float fpsCount;
    private float fpsTime;
    private boolean crossfade;
    private World next_world;

    private long startTime;
    private boolean isFading;
    private float speed;
    private static float curAlpha;
    private float newAlpha;
    private float startAlpha;

    long next_tick;
    long cur = RenderService.getTime();
    long now;
    long started;
    private boolean scaled;

    private long fadeStartTime;
    private float fadeDuration;

    @Override
    public void start() {
        try {
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
            glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            glLoadIdentity();
            curAlpha = 1f;

            System.out.println("OpenGL version: " + glGetString(GL_VERSION));

            System.out.println("Building shaders..");
            long ms = RenderService.getTime();
            ShaderFactory.buildAllShaders();
            System.out.println("Done! Took " + (RenderService.getTime() - ms) + "ms.");

        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            if (looping) {
                looping = false;
                Display.destroy();
            }
        }


        started = RenderService.getTime();
        next_tick = RenderService.getTime();
    }

    private long getTimeSinceStartMillis() {
        return RenderService.getTime() - started;
    }

    @Override
    public void render() {
        if (!RenderService.isInRenderThread())
            return;

        cur = now;
        now = getTimeSinceStartMillis();
        RenderService.TIME_DELTA = (now - cur) / 100.0f;
        if (current_world != null) {
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
            glMatrixMode(GL_MODELVIEW);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glLoadIdentity();
            glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);

            glTranslatef(-Camera.getX(), -Camera.getY(), 0f);
            ShaderFactory.executePreRender();


            if (isFading) {
                long time = RenderService.getTime() - startTime;
                curAlpha = Camera.ease(startAlpha, newAlpha, speed, time);
                if (curAlpha == newAlpha)
                    isFading = false;
            }

            glColor4f(1f, 1f, 1f, curAlpha);
            Iterator<Drawable> sprites = current_world.getSortedDrawables();
            while (sprites.hasNext()) {
                Drawable s = sprites.next();
                if (s == null)
                    continue;
                try {
                        /*if (d instanceof Sprite) {
                            if (Camera.isOffScreen((Sprite)d, 2))
                                continue;
                        } else if (Camera.isOffScreen(d.getX(), d.getY(), d.getWidth() / 2, d.getHeight() / 2, 2)) //Assume everything is 32x32
                            continue;*/
                    if ((!(s instanceof TileObject) || !((TileObject)s).isParallaxLayer()) && Camera.isOffScreen(s.getX(), s.getY(), s.getWidth() / 2, s.getHeight() / 2))
                        continue;
                    s.render();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            ShaderFactory.executePostRender();

            glLoadIdentity();
            glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);

            for (UI e : current_world.getElements()) {
                if (e == null)
                    return;
                try {
                    e.render();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }


            if (next_world != null) {
                long time = RenderService.getTime() - fadeStartTime;
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
                        /*if (d instanceof Sprite) {
                            if (Camera.isOffScreen((Sprite)d, 2))
                                continue;
                        } else if (Camera.isOffScreen(d.getX(), d.getY(), d.getWidth() / 2, d.getHeight() / 2, 2)) //Assume everything is 32x32
                            continue;*/
                        if (Camera.isOffScreen(d.getX(), d.getY(), d.getWidth() / 2, d.getHeight() / 2))
                            continue;
                        d.render();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }

                glLoadIdentity();
                glScalef(RenderService.ZOOM_SCALE, RenderService.ZOOM_SCALE, 1f);

                for (UI e : next_world.getElements()) {
                    if (e == null)
                        return;
                    try {
                        e.render();
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
            exitOnGLError("RenderService.renderSprites");

            Display.update();

            fpsTime += RenderService.TIME_DELTA;
            fpsCount++;
            if (fpsCount == 100) {
                fpsCount = 0;
                Display.setTitle("FPS: " + (1000f/fpsTime));
                fpsTime = 0;
            }
            if (Display.isCloseRequested()) {
                RenderService.kill();
            }

            if (GameSettings.Graphics.FPSLimit != -1) {
                Display.sync(GameSettings.Graphics.FPSLimit);
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

    @Override
    public void terminate() {
        current_world = null;
        next_world = null;

        Display.destroy();
    }

    @Override
    public void fadeToBlack(float speed) {
        fadeToAlpha(speed, 0f);
    }

    @Override
    public void fadeFromBlack(float speed) {
        fadeToAlpha(speed, 1f);
    }

    @Override
    public void fadeToAlpha(float speed, float alpha) {
        if (speed < 5) {
            curAlpha = alpha;
            return;
        }
        isFading = true;
        newAlpha = alpha;
        startAlpha = curAlpha;
        this.speed = speed;
        startTime = RenderService.getTime();
    }

    @Override
    public void waitForFade() throws InterruptedException {
        if (current_world == null)
            return;
        while (isFading) {
            Thread.sleep((long) speed);
        }
    }

    @Override
    public boolean isFading() {
        return isFading;
    }

    @Override
    public boolean isCrossFading() {
        return crossfade;
    }

    @Override
    public float getCurrentAlphaValue() {
        return curAlpha;
    }

    @Override
    public void removeScale() {
        if (scaled) {
            glScalef(0.5f, 0.5f, 1f);
            scaled = false;
        }
    }

    @Override
    public void resetScale() {
        if (!scaled) {
            glScalef(2f, 2f, 1f);
            scaled = true;
        }
    }

    @Override
    public void provideData(Object obj, int type) {
        Validator.validateNotNull(obj, "object");
        if (type == RenderService.WORLD_DATA_TYPE) {
            Validator.validateClass(obj, World.class);

            if (!crossfade)
                this.current_world = (World) obj;
            else {
                this.next_world = (World) obj;
                fadeStartTime = RenderService.getTime();
            }
        } else if (type == RenderService.ENABLE_CROSS_FADE) {
            this.crossfade = (boolean)obj;
        } else if (type == RenderService.CROSS_FADE_DURATION && crossfade) {
            this.fadeDuration = (float)obj;
        }
    }

    @Override
    public World getCurrentDrawingWorld() {
        return current_world;
    }
}
