package com.tog.framework.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SoundSystem {
    private List<Sound> loadedSounds = new ArrayList<>();

    public Sound loadSound(String name, String filePath) throws IOException {
        if (exists(name)) {
            throw new IllegalArgumentException("The specified sound name is already registered!");
        }

        filePath = "resources" + File.separator + "sound" + File.separator + filePath;

        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filePath));
        WaveData wavFile = WaveData.create(stream);
        int buffer = AL10.alGenBuffers();
        AL10.alBufferData(buffer, wavFile.format, wavFile.data, wavFile.samplerate);
        stream.close();
        wavFile.dispose();

        int source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);

        Sound sound = new Sound(name, source, buffer);
        loadedSounds.add(sound);

        return sound;
    }

    public boolean exists(String name) {
        for (Sound sound : loadedSounds) {
            if (sound.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Sound getSound(String name) {
        for (Sound sound : loadedSounds) {
            if (sound.getName().equals(name)) {
                return sound;
            }
        }
        return null;
    }


    public void unloadSound(String name) {
        Sound sound = getSound(name);

        if (sound == null) {
            return;
        }

        AL10.alDeleteBuffers(sound.getBuffer());
        loadedSounds.remove(sound);
    }
}
