package com.tog.framework.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public final class Sound {
    private final static DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
    private final String name;
    private final String fileName;
    private final int source;
    private final int buffer;

    private float speed;
    private float volume;

    private SoundState state;
    private OnSoundFinishedListener mSoundFinishedListener;

    private boolean alLoop = false;
    private long startTime = -1;
    private long endTime = -1;
    private int loopCount = -1;
    private int currentLoop = 0;

    private Timer timer;
    private TimerTask task;

    private void readSoundInfo() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("sound/" + fileName + ".xml");

        if (in == null) {
            return;
        }

        try {
            DocumentBuilder builder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            Document document = builder.parse(in);

            Element element = document.getDocumentElement();

            if (element.hasAttribute("alloop")) {
                alLoop = element.getAttribute("alloop").toLowerCase(Locale.ENGLISH).equals("true");
                AL10.alSourcei(source, AL10.AL_LOOPING, alLoop ? AL10.AL_TRUE : AL10.AL_FALSE);
            }

            if (element.hasAttribute("start")) {
                startTime = (long) Double.parseDouble(element.getAttribute("start"));
            }

            if (element.hasAttribute("end")) {
                endTime = (long) Double.parseDouble(element.getAttribute("end"));
            }

            if (element.hasAttribute("count")) {
                loopCount = Integer.parseInt(element.getAttribute("count"));
            }

            in.close();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }


    }

    protected Sound(String name, String fileName, int source, int buffer) {
        this.name = name;
        this.fileName = fileName;
        this.source = source;
        this.buffer = buffer;

        speed = 1F;
        volume = 1F;
        state = SoundState.STOPPED;
        timer = new Timer(name + "_timer");

        try {
            readSoundInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (state == SoundState.PLAYING) {
            stop();
        }

        AL10.alSourcePlay(source);

        if (alLoop) {
            AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
        } else if (startTime != -1 && endTime != -1) {
            AL10.alSourcef(source, AL11.AL_SEC_OFFSET, startTime);

            task = new TimerTask() {
                @Override
                public void run() {
                    currentLoop++;
                    onFinished();
                    if (currentLoop <= loopCount) {
                        AL10.alSourcef(source, AL11.AL_SEC_OFFSET, startTime);
                    } else {
                        stop();
                    }
                }
            };

            timer.scheduleAtFixedRate(task, 0L, (endTime - startTime) * 1000L);
        }

        state = SoundState.PLAYING;
    }

    private void onFinished() {
        if (mSoundFinishedListener != null) {
            mSoundFinishedListener.onFinished(this, currentLoop <= loopCount);
        }
    }

    public void setSoundFinishedListener(OnSoundFinishedListener event) {
        this.mSoundFinishedListener = event;
    }

    public OnSoundFinishedListener getSoundFinishedListener() {
        return mSoundFinishedListener;
    }

    public void stop() {
        AL10.alSourceStop(source);

        if (startTime != -1 && endTime != -1) {
            task.cancel();
            timer.cancel();
            timer.notify();
            currentLoop = 0;
        }

        state = SoundState.STOPPED;
    }

    public void pause() {
        if (state == SoundState.PLAYING) {
            AL10.alSourcePause(source);
            state = SoundState.PAUSED;
        } else if (state == SoundState.PAUSED) {
            AL10.alSourcePlay(source);
            state = SoundState.PLAYING;
        }
    }

    public String getName() {
        return name;
    }

    public int getSource() {
        return source;
    }

    public int getBuffer() {
        return buffer;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        AL10.alSourcef(source, AL10.AL_PITCH, speed);
        this.speed = speed;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        if (volume < 0) {
            volume = 0;
        } else if (volume > 1) {
            volume = 1;
        }

        AL10.alSourcef(source, AL10.AL_GAIN, volume);

        this.volume = volume;
    }

    public SoundState getState() {
        return state;
    }

    public interface OnSoundFinishedListener {
        /**
         * This method is invoked when a Sound has finished
         * @param sound The sound that finished
         * @param willLoop Whether the sound will loop
         */
        public void onFinished(Sound sound, boolean willLoop);
    }
}
