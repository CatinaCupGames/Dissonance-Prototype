package com.tog.framework.render;

import com.tog.framework.game.input.InputListener;
import com.tog.framework.game.input.InputService;
import com.tog.framework.game.sprites.animation.AnimationFactory;
import com.tog.framework.game.world.World;
import com.tog.framework.sound.Sound;
import com.tog.framework.sound.SoundState;
import com.tog.framework.sound.SoundSystem;
import com.tog.framework.system.Game;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.utils.Validator;
import net.java.games.input.Component;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluErrorString;

public class RenderService extends Service {

    public static final int WORLD_DATA_TYPE = 0;
    public static RenderService INSTANCE;
    public static float TIME_DELTA;
    public static long RENDER_THREAD_ID;
    private final ArrayList<Runnable> toRun = new ArrayList<>();
    private World current_world;
    private boolean drawing;
    private boolean looping;

    private float rotx, roty;
    private float posx, posy;

    SoundSystem soundSystem = new SoundSystem();
    Sound shot, song, town, bump;
    InputListener inputListener;

    long cur = System.currentTimeMillis();
    long now;

    @Override
    protected void onStart() {

        drawing = true;
        INSTANCE = this;

        RENDER_THREAD_ID = Thread.currentThread().getId();

        try {
            Display.setDisplayMode(new DisplayMode(Game.GAME_WIDTH, Game.GAME_HEIGHT));
            Display.create();
            AL.create();

            glClearColor(0f, 0f, 0f, 1f);
            glClearDepth(1f);
            glViewport(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0.0f, Game.GAME_WIDTH, Game.GAME_HEIGHT, 0.0f, 0f, 1f);
            //gluPerspective(90, Game.GAME_WIDTH / Game.GAME_HEIGHT, 0.1f, 10000);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glLoadIdentity();

            shot = soundSystem.loadSound("shot", "shotproto.wav");
            song = soundSystem.loadSound("song", "song1.wav");
            town = soundSystem.loadSound("town", "town.wav");
            bump = soundSystem.loadSound("bump", "bump.wav");

        } catch (LWJGLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (looping) {
                looping = false;
                Display.destroy();
            }
        }


        inputListener = new InputListener() {
            private List<Integer> keys = new ArrayList<>();
            private List<Integer> buttons = new ArrayList<>();

            @Override
            public List<Integer> getKeys() {
                return keys;
            }

            @Override
            public List<Integer> getButtons() {
                return buttons;
            }

            @Override
            public void inputPressed(int key) {
                //NOTE: Never collect keys for sprite movement here //
                switch (key) {
                    case Keyboard.KEY_T:
                        town.play();
                        break;
                    case Keyboard.KEY_R:
                        town.stop();
                        break;
                    case Keyboard.KEY_M:
                        if (song.getState() == SoundState.PLAYING) {
                            song.stop();
                        } else {
                            song.play();
                        }
                        break;
                    case Keyboard.KEY_G:
                        if (shot.getState() == SoundState.PLAYING) {
                            shot.stop();
                        } else {
                            shot.play();
                        }
                        break;
                    case Keyboard.KEY_ADD:
                        town.setVolume(town.getVolume() + 0.1F);
                        song.setVolume(song.getVolume() + 0.1F);
                        shot.setVolume(shot.getVolume() + 0.1F);
                        bump.setVolume(bump.getVolume() + 0.1F);
                        break;
                    case Keyboard.KEY_SUBTRACT:
                        town.setVolume(town.getVolume() - 0.1F);
                        song.setVolume(song.getVolume() - 0.1F);
                        shot.setVolume(shot.getVolume() - 0.1F);
                        bump.setVolume(bump.getVolume() - 0.1F);
                        break;
                    case Keyboard.KEY_MULTIPLY:
                        town.setSpeed(town.getSpeed() + 0.1F);
                        song.setSpeed(song.getSpeed() + 0.1F);
                        shot.setSpeed(shot.getSpeed() + 0.1F);
                        bump.setSpeed(bump.getSpeed() + 0.1F);
                        break;
                    case Keyboard.KEY_DIVIDE:
                        town.setSpeed(town.getSpeed() - 0.1F);
                        song.setSpeed(song.getSpeed() - 0.1F);
                        shot.setSpeed(shot.getSpeed() - 0.1F);
                        bump.setSpeed(bump.getSpeed() - 0.1F);
                        break;
                    case Keyboard.KEY_LCONTROL:
                        town.stop();
                        song.stop();
                        shot.stop();
                        bump.stop();
                        break;
                }
            }

            @Override
            public void inputClicked(int button, int x, int y) {
                if (button == 0) {  //LMB
                    soundSystem.getSound("shot").play();
                } else if (button == 1) { //RMB
                    soundSystem.getSound("bump").play();
                }
                System.out.println(Camera.getX() + ":" + Camera.getY());
            }
        };

        Service inputService = ServiceManager.createService(InputService.class);
        inputListener.getKeys().addAll(Arrays.asList(
                Keyboard.KEY_E, Keyboard.KEY_Q, Keyboard.KEY_Z, Keyboard.KEY_X, Keyboard.KEY_T,
                Keyboard.KEY_M, Keyboard.KEY_G, Keyboard.KEY_L, Keyboard.KEY_ADD, Keyboard.KEY_SUBTRACT,
                Keyboard.KEY_MULTIPLY, Keyboard.KEY_DIVIDE, Keyboard.KEY_LCONTROL, Keyboard.KEY_R));

        inputListener.getButtons().add(0);
        inputListener.getButtons().add(1);

        inputService.provideData(inputListener, InputService.ADD_LISTENER);

        // Just a simple test for you guys!!!
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while(true)
                {
                    // Example with name.
                    //System.out.println(InputService.getButtonState("Logitech Driving Force USB", "Cross"));
                    // Example with identifier.
                    //System.out.println(InputService.getButtonState("Logitech Driving Force USB", Component.Identifier.Button._0));

                    // Example with name.
                    //System.out.println(InputService.getAxisValue("Logitech Driving Force USB", "Wheel axis"));
                    // Example with identifier.
                    System.out.println(InputService.getAxisValue("Logitech Driving Force USB", Component.Identifier.Axis.X));
                }
            }
        }).start();
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
        soundSystem.unloadAllSounds();
        current_world = null;
        INSTANCE = null;
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
    public void onUpdate() {
        now = System.currentTimeMillis();
        TIME_DELTA = ((now - cur) / 100.0f);
        if (current_world != null && !isPaused()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(1f, 1f, 1f, 1f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            glRotatef(-rotx, 1, 0, 0);
            glRotatef(-roty, 0, 1, 0);
            glRotatef(0, 0, 0, 1);
            glScalef(2.5f, 2.5f, 1f);
            glTranslatef(-Camera.getX(), -Camera.getY(), 0f);

            Iterator<Drawable> sprites = current_world.getDrawable();
            while (sprites.hasNext()) {
                Drawable s = sprites.next();
                if (s == null)
                    continue;
                try {
                    s.update();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            sprites = current_world.getDrawable();
            while (sprites.hasNext()) {
                Drawable s = sprites.next();
                if (s == null)
                    continue;
                try {
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

            exitOnGLError("RenderService.renderSprites");

            Display.update();
            cur = now;
            if (Display.isCloseRequested()) {
                ServiceManager.getService(InputService.class).terminate();
                terminate();
                Game.getSystemTicker().stopTick();
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