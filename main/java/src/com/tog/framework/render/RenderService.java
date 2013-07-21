package com.tog.framework.render;

import com.tog.framework.game.input.InputListener;
import com.tog.framework.game.input.InputService;
import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.game.world.World;
import com.tog.framework.sound.Sound;
import com.tog.framework.sound.SoundState;
import com.tog.framework.sound.SoundSystem;
import com.tog.framework.system.Game;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.utils.Validator;
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
import static org.lwjgl.util.glu.GLU.gluPerspective;

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

            this.current_world = (World) obj;
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
            AL.create();

            glClearColor(0f, 0f, 0f, 1f);
            glClearDepth(1f);
            glViewport(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            gluPerspective(90, Game.GAME_WIDTH / Game.GAME_HEIGHT, 0.1f, 10000);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_TEXTURE_2D);
            glLoadIdentity();

            //TEMP CODE BEGIN
            final SoundSystem soundSystem = new SoundSystem();
            final Sound shot, song, town, bump;

            try {
                shot = soundSystem.loadSound("shot", "shotproto.wav");
                song = soundSystem.loadSound("song", "song1.wav");
                town = soundSystem.loadSound("town", "town.wav");
                bump = soundSystem.loadSound("bump", "bump.wav");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Service inputService = ServiceManager.createService(InputService.class);

            InputListener listener = new InputListener() {
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
                public void inputPressed(Integer key) {
                    switch (key) {
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
                        case Keyboard.KEY_T:
                            if (town.getState() == SoundState.PLAYING) {
                                town.stop();
                            } else {
                                town.play();
                            }
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
                        case Keyboard.KEY_L: {
                            shot.setLooping(!shot.isLooping());
                        }
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
                public void inputClicked(Integer button) {
                    if (button == 0) {  //LMB
                        soundSystem.getSound("shot").play();
                    } else if (button == 1) { //RMB
                        soundSystem.getSound("bump").play();
                    }
                }
            };
            listener.getKeys().addAll(Arrays.asList(Keyboard.KEY_E, Keyboard.KEY_D, Keyboard.KEY_A,
                    Keyboard.KEY_S, Keyboard.KEY_Q, Keyboard.KEY_W, Keyboard.KEY_Z, Keyboard.KEY_X, Keyboard.KEY_T,
                    Keyboard.KEY_M, Keyboard.KEY_G, Keyboard.KEY_L, Keyboard.KEY_ADD, Keyboard.KEY_SUBTRACT,
                    Keyboard.KEY_MULTIPLY, Keyboard.KEY_DIVIDE, Keyboard.KEY_LCONTROL));
            listener.getKeys().add(Keyboard.KEY_E);

            listener.getButtons().add(0);
            listener.getButtons().add(1);

            inputService.provideData(listener, InputService.ADD_LISTENER);
            //TEMP CODE END

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
                        glTexCoord3f(s.getX() + cx, s.getY(), 0f);
                        glVertex3f(s.getX() - bx, s.getY() + by, 0f);
                        glTexCoord3f(s.getX(), s.getY(), 0f);
                        glVertex3f(s.getX() + bx, s.getY() + by, 0f);
                        glTexCoord3f(s.getX(), s.getY() + cy, 0f);
                        glVertex3f(s.getX() + bx, s.getY() - by, 0f);
                        glTexCoord3f(s.getX() + cx, s.getY() + cy, 0f);
                        glVertex3f(s.getX() - bx, s.getY() - by, 0f);
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
