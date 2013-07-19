package com.tog.framework.sound;

import org.lwjgl.openal.AL10;

public final class Sound {
    private String name;
    private int source;
    private int buffer;

    private float speed;
    private float volume;

    private SoundState state;

    protected Sound(String name, int source, int buffer) {
        this.name = name;
        this.source = source;
        this.buffer = buffer;

        speed = 1F;
        volume = 1F;
        state = SoundState.STOPPED;
    }

    public void play() {
        AL10.alSourcePlay(source);
        state = SoundState.PLAYING;
    }

    public void stop() {
        AL10.alSourceStop(source);
        state = SoundState.STOPPED;
    }

    public void rewind(boolean play) {
        AL10.alSourceRewind(source);

        if (play) {
            play();
        }
    }

    public void pause() {
        if (state == SoundState.STOPPED) {
            return;
        } else if (state == SoundState.PLAYING) {
            AL10.alSourcePause(source);
            state = SoundState.PAUSED;
        } else if (state == SoundState.PAUSED) {
            AL10.alSourcePlay(source);
            state = SoundState.PLAYING;
        }
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

    public String getName() {
        return name;
    }

    public int getSource() {
        return source;
    }

    public int getBuffer() {
        return buffer;
    }
}
