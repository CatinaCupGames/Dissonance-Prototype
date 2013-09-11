package com.dissonance.framework.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class SoundSystem {
    private List<Sound> loadedSounds = new ArrayList<>();

    public Sound loadSound(String name, String filePath) throws IOException {
        if (exists(name)) {
            throw new IllegalArgumentException("The specified sound name is already registered!");
        }
        WaveData wavFile;
        String resource = "sounds" + File.separator + filePath;
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            resource = "sounds/" + filePath;
            stream = getClass().getClassLoader().getResourceAsStream(resource);
            if (stream == null)
                throw new IOException("Can't find sound \"" + resource + "\"!");
        }
        wavFile = WaveData.create(stream);
        if (wavFile == null) {
            URL url = getClass().getClassLoader().getResource(resource);
            wavFile = WaveData.create(url);
            if (wavFile == null)
                throw new IOException("Failed to create WaveData for \"" + resource + "\"!");
        }
        int buffer = AL10.alGenBuffers();
        AL10.alBufferData(buffer, wavFile.format, wavFile.data, wavFile.samplerate);
        stream.close();
        wavFile.dispose();

        int source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);

        Sound sound = new Sound(name, filePath.substring(0, filePath.length() - 4), source, buffer);
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

    public void unloadAllSounds() {
        for (Sound sound : loadedSounds) {
            AL10.alDeleteBuffers(sound.getBuffer());
        }
        loadedSounds.clear();
    }
}
