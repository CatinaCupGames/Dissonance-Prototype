package com.dissonance.framework.render;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.game.input.InputService;
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

import javax.xml.soap.Text;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluErrorString;

public class RenderService extends Service {
    public static final int WORLD_DATA_TYPE = 0;
    public static final int ENABLE_CROSS_FADE = 1;
    public static final int CROSS_FADE_DURATION = 2;
    public static final int RENDERER_DATA_TYPE = 4;
    public static final float ZOOM_SCALE = 2f;
    public static RenderService INSTANCE;

    /****************************************************
     * RENDER SETTINGS                                  *
     ***************************************************/
    /*public static int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 720;
    public static double GAME_WIDTH = (double)WINDOW_WIDTH, GAME_HEIGHT = (double)WINDOW_HEIGHT; // TODO: For now the resolution will be the same as the window size;
    public static boolean fullscreen = true; //TODO Create config to change this value*/

    public static float TIME_DELTA;
    public static long RENDER_THREAD_ID;
    private Renderer renderer;

    public static boolean isInRenderThread() {
        return Thread.currentThread().getId() == RENDER_THREAD_ID;
    }

    public void fadeToBlack(float speed) {
        fadeToAlpha(speed, 0f);
    }

    public void fadeFromBlack(float speed) {
        fadeToAlpha(speed, 1f);
    }

    public void fadeToAlpha(float speed, float alpha) {
        if (renderer != null)
            renderer.fadeToAlpha(speed, alpha);
    }

    public boolean isCrossFading() {
        return renderer != null && renderer.isCrossFading();
    }

    public static float getCurrentAlphaValue() {
        if (INSTANCE != null && INSTANCE.renderer != null)
            return INSTANCE.renderer.getCurrentAlphaValue();
        return 1f;
    }

    public void waitForFade() throws InterruptedException {
        if (renderer != null)
            renderer.waitForFade();
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

    ByteBuffer[] icons;
    private void loadIcons() throws IOException {
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if (OS.contains("win")) {  //Expects one 32x32 and one 16x16
            icons = new ByteBuffer[2];
            icons[0] = TextureLoader.convertImageData(TextureLoader.loadImage("icon_32.png"), null, true);
            icons[1] = TextureLoader.convertImageData(TextureLoader.loadImage("icon_16.png"), null, true);
        }
        else if (OS.contains("mac")) { //Expects one 128x128
            icons = new ByteBuffer[1];
            icons[0] = TextureLoader.convertImageData(TextureLoader.loadImage("icon_128.png"), null, true);
        }
        else {  //Expects one 32x32
            icons = new ByteBuffer[1];
            icons[0] = TextureLoader.convertImageData(TextureLoader.loadImage("icon_32.png"), null, true);
        }
    }


    @Override
    protected void onStart() {
        INSTANCE = this;

        RENDER_THREAD_ID = Thread.currentThread().getId();

        try {
            loadIcons();
            Display.setIcon(icons);
            setDisplayMode(GameSettings.Display.window_width, GameSettings.Display.window_height, GameSettings.Display.fullscreen);

            try {
                renderer = (Renderer)Class.forName(GameSettings.Graphics.rendererClass).newInstance();
                renderer.start();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (renderer != null)
            renderer.terminate();

        renderer = null;
        INSTANCE = null;
    }

    private static void killAll() {
        //Kill any waiting threads
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState() == Thread.State.WAITING && !t.getName().contains("Disposer")) {
                t.interrupt();
            }
        }
    }

    public static void removeScale() {
        if (INSTANCE != null && INSTANCE.renderer != null)
            INSTANCE.renderer.removeScale();
    }

    public static void resetScale() {
        if (INSTANCE != null && INSTANCE.renderer != null)
            INSTANCE.renderer.resetScale();
    }

    @Override
    public void provideData(Object obj, int type) {
        Validator.validateNotNull(obj, "object");
        if (type == RENDERER_DATA_TYPE) {
            Validator.validateClass(obj, Renderer.class);

            this.renderer.terminate();
            this.renderer = (Renderer)obj;
            this.renderer.start();
        } else if (renderer != null) {
            renderer.provideData(obj, type);
        }
    }

    @Override
    protected String getName() {
        return "RenderService Thread";
    }

    public static ContextCapabilities getCapabilities() {
        if (!isInRenderThread())
            throw new RuntimeException("You must be in the render thread to get capabilities!");
        return GLContext.getCapabilities();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000;
    }

    @Override
    public void onUpdate() {
        if (isPaused() || renderer == null) {
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            renderer.render();
        }
    }

    public World getCurrentDrawingWorld() {
        if (renderer != null)
            return renderer.getCurrentDrawingWorld();
        return null;
    }

    public boolean isFading() {
        return renderer != null && renderer.isFading();
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