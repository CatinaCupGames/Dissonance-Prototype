package com.dissonance.framework.sound;

import com.dissonance.framework.system.utils.Validator;
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
import java.util.ArrayList;

import static org.lwjgl.openal.AL10.*;

public final class Sound {
    private static ArrayList<Sound> sounds = new ArrayList<>();
    private static volatile boolean running = false;

    private final String name;
    private final int bufferIndex;
    private final int sourceIndex;

    protected final float startTime;
    protected final float endTime;

    private Sound(String name, int bufferIndex, int sourceIndex, float startTime, float endTime) {
        this.name = name;
        this.bufferIndex = bufferIndex;
        this.sourceIndex = sourceIndex;

        this.startTime = startTime;
        this.endTime = endTime;
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

    private static String getError(int error) {
        switch(error) {
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

    private static int loadSound(String name, String path, float startTime, float endTime) {
        IntBuffer source = BufferUtils.createIntBuffer(1);
        int buffer = loadALBuffer(path);

        alGenSources(source);
        checkError();

        alSourcei(source.get(0), AL10.AL_BUFFER, buffer);

        sounds.add(new Sound(name, buffer, source.get(0), startTime, endTime));

        return source.get(0);
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

                    if (sound.hasAttribute("start") && sound.hasAttribute("end")) {
                        float startTime = parseTime(sound.getAttribute("start"));
                        float endTime = parseTime(sound.getAttribute("end"));

                        loadSound(soundName, ("sounds/" + soundName + ".wav"), startTime, endTime);
                    } else {
                        loadSound(soundName, ("sounds/" + soundName + ".wav"), -1f, -1f);
                    }
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

        Thread loopThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    for (Sound sound : sounds) {
                        if (sound.endTime != -1f && sound.startTime != -1f) {
                            float offset = alGetSourcef(sound.getSourceIndex(), AL11.AL_SEC_OFFSET);

                            if (offset >= sound.endTime) {
                                alSourcef(sound.getSourceIndex(), AL11.AL_SEC_OFFSET, sound.startTime);
                                alSourceStop(sound.getSourceIndex());
                                alSourcePlay(sound.getSourceIndex());

                                System.out.println("loopity");
                            }
                        }
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
     *
     * @return The sound if found, otherwise null.
     */
    public static Sound getSound(String name) {
        for (Sound sound : sounds) {
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
     */
    public static void playSound(String name) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourcePlay(sound.getSourceIndex());
        }
    }

    /**
     * Pauses a sound with the specified name. If the sound
     * can't be found, nothing will happen. Use {@link #playSound(String)}
     * to resume the sound.
     *
     * @param name The name of the sound to pause.
     */
    public static void pauseSound(String name) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourcePause(sound.getSourceIndex());
        }
    }

    /**
     * Stops a sound with the specified name. If the sound
     * can't be found, nothing will happen. Use {@link #playSound(String)}
     * to play the sound.
     *
     * @param name The name of the sound to stop.
     */
    public static void stopSound(String name) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourceStop(sound.getSourceIndex());
        }
    }

    /**
     * Sets the pitch of the sound with the specified name.
     * If the sound can't be found, nothing will happen. <br />
     * The specified pitch value should be between <code>0.5</code>
     * and <code>2.0</code>. The default value is 1.0
     *
     * @param name The name of the sound.
     * @param pitch The new pitch of the sound.
     *
     * @throws java.security.InvalidParameterException If the specified
     * pitch is out of range.
     */
    public static void setPitch(String name, float pitch) {
        Validator.validateInRange(pitch, 0.5, 2.0, "pitch");

        Sound sound = getSound(name);

        if (sound != null) {
            alSourcef(sound.getSourceIndex(), AL_PITCH, pitch);
        }
    }

    /**
     * Sets the volume of the sound with the specified name.
     * If the sound can't be found, nothing will happen. <br />
     * The default value is <code>1.0</code>. Each multiplication by
     * two equals an amplification of <code>+6dB</code> while a division
     * by two equals an attenuation of <code>-6dB</code>. A value of
     * <code>0</code> will effectively disable the sound.
     */
    public static void setVolume(String name, float volume) {
        Sound sound = getSound(name);

        if (sound != null) {
            alSourcef(sound.getSourceIndex(), AL_GAIN, volume);
        }
    }
}
