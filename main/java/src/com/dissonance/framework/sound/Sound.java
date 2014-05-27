package com.dissonance.framework.sound;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Validator;
import com.sun.jndi.url.dns.dnsURLContext;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.WaveData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;

public final class Sound {
    private static ArrayList<Sound> sounds = new ArrayList<>();
    private static ArrayList<Integer> sources = new ArrayList<>();
    private static ArrayList<Sound> addQueue = new ArrayList<>();
    private static volatile boolean running = false;
    protected final float startTime;
    protected final float endTime;
    protected float volume;
    private final String name;
    private int bufferIndex;
    private int sourceIndex;
    private final boolean soundEffect;
    private final boolean temp;
    private final String path;
    protected int lastKnownState = AL_STOPPED; //Used to check if non-looping sounds ended.
    protected OnSoundFinishedListener listener;

    private Sound(String name, int bufferIndex, int sourceIndex, float startTime, float endTime, boolean soundEffect, String path) {
        this(name, bufferIndex, sourceIndex, startTime, endTime, soundEffect, false, path);
    }

    private Sound(String name, int bufferIndex, int sourceIndex, float startTime, float endTime, boolean soundEffect, boolean clone, String path) {
        this.name = name;
        this.bufferIndex = bufferIndex;
        this.sourceIndex = sourceIndex;

        this.startTime = startTime;
        this.endTime = endTime;
        this.soundEffect = soundEffect;
        this.temp = clone;
        this.path = path;
    }

    private static String getError(int error) {
        switch (error) {
            case AL_NO_ERROR:
                return "AL_NO_ERROR";
            case AL_INVALID_NAME:
                return "AL_INVALID_NAME";
            case AL_INVALID_ENUM:
                return "AL_INVALID_ENUM";
            case AL_INVALID_VALUE:
                return "AL_INVALID_VALUE";
            case AL_INVALID_OPERATION:
                return "AL_INVALID_OPERATION";
            case AL_OUT_OF_MEMORY:
                return "AL_OUT_OF_MEMORY";
            default:
                return "UNKNOWN " + error;
        }
    }

    private static void checkError() {
        int error = alGetError();

        if (error != AL_NO_ERROR) {
            System.out.println("An error occured: " + getError(error));
        }
    }

    private static int loadALBuffer(String path) {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);

        alGenBuffers(buffer);
        checkError();

        WaveData waveData = WaveData.create(path);

        if (waveData != null) {
            alBufferData(buffer.get(0), waveData.format, waveData.data, waveData.samplerate);
            waveData.dispose();
        } else {
            throw new RuntimeException("An error occured while trying to load " + path);
        }

        return buffer.get(0);
    }

    private static void loadSound(String name, String path, float startTime, float endTime, boolean soundEffect) {
        //int buffer = loadALBuffer(path); Do not load buffers until requested
        //checkError();
        sounds.add(new Sound(name, -1, -1, startTime, endTime, soundEffect, path)); //Set source to -1, only give a source when the sound is played
    }

    private static int generateNewSource() {
        IntBuffer source = BufferUtils.createIntBuffer(1);
        alGenSources(source);
        sources.add(source.get(0));
        return source.get(0);
    }

    private static int getFreeSource() {
        for (Integer source : sources) {
            int value = source;
            boolean found = true;
            for (Sound s : sounds) {
                if (value == s.sourceIndex && (!s.isSoundEffect() || s.lastKnownState == AL_PLAYING || s.lastKnownState == AL_PAUSED)) {
                    found = false;
                    break;
                } else if (value == s.sourceIndex && s.isSoundEffect() && s.lastKnownState == AL_STOPPED) {
                    s.sourceIndex = -1; //Remove this source index to prevent conflict
                }
            }

            for (Sound s : addQueue) { //Check the addQueue as well, however cannot steal from here
                if (value == s.sourceIndex) {
                    found = false;
                    break;
                }
            }

            if (!found)
                continue;
            return value;
        }
        return generateNewSource();
    }

    public static Sound cloneSound(Sound source) {
        if (source.startTime != -1 || source.endTime != -1 || !source.isSoundEffect())
            throw new InvalidParameterException("Only sound effects that don't loop can be cloned!");

        Sound s = new Sound(source.name, source.bufferIndex, getFreeSource(), source.startTime, source.endTime, source.soundEffect, true, source.path);

        alSourcei(s.sourceIndex, AL10.AL_BUFFER, s.bufferIndex);

        addQueue.add(s);

        return s;
    }

    /**
     * Loads all of the sounds listed in the sounds/_sounds.xml file.
     * The name of the sound will be the file name without the .wav extension.
     * <code>example.wav</code> would have the name <code>example</code>.
     */
    public static void loadAllSounds() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        try (InputStream in = Sound.class.getClassLoader().getResourceAsStream("sounds/_sounds.xml")) {
            if (in == null) {
                return;
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(in);
            document.normalize();

            Element element = document.getDocumentElement();
            NodeList sounds = element.getElementsByTagName("sound");

            for (int i = 0; i < sounds.getLength(); i++) {
                Node soundNode = sounds.item(i);

                if (soundNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element sound = (Element) soundNode;

                    String soundName = sound.getAttribute("name");
                    float startTime = -1, endTime = -1;
                    boolean soundEffect = false;
                    if (sound.hasAttribute("start") && sound.hasAttribute("end")) {
                        startTime = parseTime(sound.getAttribute("start"));
                        endTime = parseTime(sound.getAttribute("end"));
                    }
                    if (sound.hasAttribute("soundEffect")) {
                        soundEffect = sound.getAttribute("soundEffect").toLowerCase().equals("true");
                    }
                    loadSound(soundName, ("sounds/" + soundName + ".wav"), startTime, endTime, soundEffect);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        loop();
    }

    private static float parseTime(String time) {
        float seconds = 0;

        String[] split = time.split(":");

        seconds += Integer.parseInt(split[0]) * 60;
        seconds += Integer.parseInt(split[1]);
        seconds += Integer.parseInt(split[2]) / 1000;

        return seconds;
    }

    private static void loop() {
        if (running) {
            return;
        }

        running = true;

        final ArrayList<Sound> toremove = new ArrayList<Sound>();

        Thread loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    for (Sound sound : sounds) {
                        if (sound.endTime != -1f && sound.startTime != -1f) { //Looping
                            float offset = alGetSourcef(sound.getSourceIndex(), AL11.AL_SEC_OFFSET);

                            if (offset >= sound.endTime) {
                                alSourcef(sound.getSourceIndex(), AL11.AL_SEC_OFFSET, sound.startTime);
                                alSourceStop(sound.getSourceIndex());
                                alSourcePlay(sound.getSourceIndex());

                                if (sound.listener != null) {
                                    sound.listener.onFinished(SoundFinishedType.LOOPED);
                                }
                            }
                        } else { //Non-looping
                            int state = alGetSourcei(sound.getSourceIndex(), AL_SOURCE_STATE);

                            if (state == AL_STOPPED && sound.lastKnownState != AL_STOPPED) {
                                sound.lastKnownState = AL_STOPPED;

                                if (sound.listener != null) {
                                    sound.listener.onFinished(SoundFinishedType.ENDED);
                                }

                                if (sound.temp) //A cloned sound
                                    toremove.add(sound);
                            }
                        }

                        if (sound.isFadingIn) {
                            float time = RenderService.getTime() - sound.fade_startTime;
                            float newVolume = (1f * (time / sound.fade_duration));
                            if (newVolume >= 1f)
                                sound.isFadingIn = false;
                            sound.setVolume(newVolume);
                        }
                        if (sound.isFadingOut) {
                            float time = RenderService.getTime() - sound.fade_startTime;
                            float newVolume = (1f * (1- (time / sound.fade_duration)));
                            if (newVolume <= 0f)
                                sound.isFadingOut = false;
                            sound.setVolume(newVolume);
                        }
                    }


                    if (toremove.size() > 0) {
                        for (Sound s : toremove) {
                            sounds.remove(s);
                        }

                        toremove.clear();
                    }

                    if (addQueue.size() > 0) {
                        sounds.addAll(addQueue);
                        addQueue.clear();
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        loopThread.start();
    }

    public static void dispose() {
        running = false;
        sounds.clear();

        AL.destroy();
    }

    /**
     * Gets a sound by its name. Sound objects can be used
     * to retrieve the buffer index and the source index of sounds
     * for custom operations that are not supported by the API.
     *
     * @param name The name of the sound.
     * @return The sound if found, otherwise null.
     */
    public static Sound getSound(String name) {
        for (Sound sound : sounds) {
            if (sound.temp)
                continue;
            if (sound.getName().equals(name)) {
                return sound;
            }
        }

        return null;
    }

    /**
     * Plays a sound with the specified name. If the sound
     * can't be found, nothing will happen. If the sound is
     * paused, it will be resumed.
     *
     * @param name The name of the sound to play.
     * @return The sound if found, otherwise null.
     */
    public static Sound playSound(String name) {
        try {
            Sound sound = getSound(name);

            if (sound != null) {
                if (sound.bufferIndex == -1) {
                    sound.bufferIndex = loadALBuffer(sound.path);
                    checkError();
                }
                if (sound.sourceIndex == -1) {
                    sound.sourceIndex = getFreeSource();
                    alSourcei(sound.sourceIndex, AL10.AL_BUFFER, sound.bufferIndex);
                }

                if (sound.lastKnownState == AL_PLAYING && sound.isSoundEffect()) {
                    Sound tempSound = cloneSound(sound);
                    alSourcePlay(tempSound.getSourceIndex());
                    if (tempSound.startTime == -1 && tempSound.endTime == -1) {
                        tempSound.lastKnownState = AL_PLAYING;
                    }
                    return tempSound;
                } else {
                    alSourcePlay(sound.getSourceIndex());

                    if (sound.startTime == -1 && sound.endTime == -1) {
                        sound.lastKnownState = AL_PLAYING;
                    }
                }
            }

            return sound;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public static Sound fadeInSound(String name, float duration) {
        Sound sound = Sound.playSound(name);

        if (sound != null) {
            sound.fadeIn(duration);
        }

        return sound;
    }

    public static Sound fadeInSound(String name) {
        return fadeInSound(name, 1700);
    }

    /**
     * Pauses a sound with the specified name. If the sound
     * can't be found, nothing will happen. Use {@link #playSound(String)}
     * to resume the sound.
     *
     * @param name The name of the sound to pause.
     * @return The sound if found, otherwise null.
     */
    public static Sound pauseSound(String name) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourcePause(sound.getSourceIndex());

            if (sound.startTime == -1 && sound.endTime == -1) {
                sound.lastKnownState = AL_PAUSED;
            }

            if (sound.listener != null) {
                sound.listener.onFinished(SoundFinishedType.PAUSED);
            }
        }

        return sound;
    }

    /**
     * Stops a sound with the specified name. If the sound
     * can't be found, nothing will happen. Use {@link #playSound(String)}
     * to play the sound.
     *
     * @param name The name of the sound to stop.
     * @return The sound if found, otherwise null.
     */
    public static Sound stopSound(String name) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourceStop(sound.getSourceIndex());

            if (sound.startTime == -1 && sound.endTime == -1) {
                sound.lastKnownState = AL_STOPPED;
            }

            if (sound.listener != null) {
                sound.listener.onFinished(SoundFinishedType.STOPPED);
            }
        }

        return sound;
    }

    /**
     * Sets the pitch of the sound with the specified name.
     * If the sound can't be found, nothing will happen. <br />
     * The specified pitch value should be between <code>0.5</code>
     * and <code>2.0</code>. The default value is 1.0
     *
     * @param name  The name of the sound.
     * @param pitch The new pitch of the sound.
     * @return The sound if found, otherwise null.
     * @throws java.security.InvalidParameterException If the specified
     *                                                 pitch is out of range.
     */
    public static Sound setPitch(String name, float pitch) {
        Validator.validateInRange(pitch, 0.5, 2.0, "pitch");

        Sound sound = getSound(name);

        if (sound != null) {
            alSourcef(sound.getSourceIndex(), AL_PITCH, pitch);
        }

        return sound;
    }

    /**
     * Sets the volume of the sound with the specified name.
     * If the sound can't be found, nothing will happen. <br />
     * The default value is <code>1.0</code>. Each multiplication by
     * two equals an amplification of <code>+6dB</code> while a division
     * by two equals an attenuation of <code>-6dB</code>. A value of
     * <code>0</code> will effectively disable the sound.
     *
     * @param name   The name of the sound.
     * @param volume The new volume of the sound.
     * @return The sound if found, otherwise null.
     */
    public static Sound setVolume(String name, float volume) {
        Sound sound = getSound(name);
        if (volume > 1f)
            volume = 1f;
        else if (volume < 0f)
            volume = 0f;

        if (sound != null) {
            alSourcef(sound.getSourceIndex(), AL_GAIN, volume);
            sound.volume = volume;
        }

        return sound;
    }

    private float fade_duration;
    private boolean isFadingIn;
    private boolean isFadingOut;
    private long fade_startTime;
    public void fadeIn() {
        fadeIn(1700);
    }

    public void fadeIn(float duration) {
        fade_duration = duration;
        fade_startTime = RenderService.getTime();
        isFadingIn = true;
        setVolume(0f);
    }

    public void fadeOut() {
        fadeOut(1700);
    }

    public void fadeOut(float duration) {
        fade_duration = duration;
        fade_startTime = RenderService.getTime();
        isFadingOut = true;
    }


    public void setVolume(float volume) {
        if (volume > 1f)
            volume = 1f;
        else if (volume < 0f)
            volume = 0f;
        alSourcef(sourceIndex, AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        alSourcef(sourceIndex, AL_PITCH, pitch);
    }

    public void stop() {
        alSourceStop(sourceIndex);

        if (startTime == -1 && endTime == -1) {
            lastKnownState = AL_STOPPED;
        }

        if (listener != null) {
            listener.onFinished(SoundFinishedType.STOPPED);
        }
    }

    public void play() {
        if (sourceIndex == -1) {
            sourceIndex = getFreeSource();
        }

        if (lastKnownState == AL_PLAYING && isSoundEffect()) {
            Sound tempSound = cloneSound(this);
            alSourcePlay(tempSound.getSourceIndex());
            if (tempSound.startTime == -1 && tempSound.endTime == -1) {
                tempSound.lastKnownState = AL_PLAYING;
            }
        } else {
            alSourcePlay(sourceIndex);

            if (startTime == -1 && endTime == -1) {
                lastKnownState = AL_PLAYING;
            }
        }
    }

    public void pause() {
        alSourcePause(sourceIndex);

        if (startTime == -1 && endTime == -1) {
            lastKnownState = AL_PAUSED;
        }

        if (listener != null) {
            listener.onFinished(SoundFinishedType.PAUSED);
        }
    }

    /**
     * Gets the name of the sound.
     *
     * @return The sound's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the index of the sound's bufferIndex.
     *
     * @return The index of the sound's bufferIndex.
     */
    public int getBufferIndex() {
        return bufferIndex;
    }

    /**
     * Gets the index of the sound's sourceIndex.
     *
     * @return The index of the sound's sourceIndex.
     */
    public int getSourceIndex() {
        return sourceIndex;
    }

    /**
     * Returns true if this Sound is a sound effect.
     *
     * @return True if this Sound is a sound effect.
     */
    public boolean isSoundEffect() {
        return soundEffect;
    }

    /**
     * Sets the OnSoundFinishedListener for this sound.
     *
     * @param listener The new sound listener.
     */
    public Sound setOnSoundFinishedListener(OnSoundFinishedListener listener) {
        this.listener = listener;
        return this;
    }

    public enum SoundFinishedType {
        /**
         * A sound was stopped by using the {@link #stopSound(String)} function.
         */
        STOPPED,

        /**
         * A sound was stopped by using the {@link #pauseSound(String)} function.
         */
        PAUSED,

        /**
         * A looping sound ended and looped again.
         */
        LOOPED,

        /**
         * The non-looping sound ended.
         */
        ENDED
    }

    public interface OnSoundFinishedListener {
        /**
         * The onFinished method is called when a sound has finished playing.
         * If a sound is looping it will be called after each loop.
         *
         * @param type Indicates how did the sound finish.
         */
        public void onFinished(SoundFinishedType type);
    }
}
