package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.AbstractShader;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class LightShader extends AbstractShader {
    private ArrayList<Light> lights = new ArrayList<Light>();
    private float overallBrightness = 0.5f;
    private int resolutionLocation = -1, overallBrightnessLocation = -1, countLocation = -1, windowLocation = -1, cameraLocation = -1, aspectLocation = -1, texture2 = -1, texture3 = -1;
    private float oX, oY;
    private int lightDataTexture = -1, colorDataTexture = -1;
    private int scaleLocation;

    @Override
    public String getVertexFile() {
        return "lights.vert";
    }

    @Override
    public String getFragmentFile() {
        return "lights.frag";
    }

    @Override
    public String getName() {
        return "Lights";
    }

    public void addAll(List<Light> lights) {
        this.lights.addAll(lights);

        set = false;
        lightUpdate = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void add(Light l) {
        if (!this.lights.contains(l))
            this.lights.add(l);

        set = false;
        lightUpdate = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void clear() {
        this.lights.clear();

        set = false;
        lightUpdate = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void remove(Light l) {
        if (this.lights.contains(l))
            this.lights.remove(l);

        set = false;
        lightUpdate = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void remove(int index) {
        this.lights.remove(index);

        set = false;
        lightUpdate = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public float getOverallBrightness() {
        return overallBrightness;
    }

    public void setOverallBrightness(float brightness) {
        this.overallBrightness = brightness;
        set = false;
    }

    @Override
    public void build() {
        super.build();
    }

    protected static boolean set = false;
    protected static boolean lightUpdate = false;

    @Override
    public void onPreRender() {
        super.onPreRender();
        final int program = getProgramID();
        if (!set) {
            if (texture2 == -1) {
                texture2 = GL20.glGetUniformLocation(program, "lightData");
            }

            if (texture3 == -1) {
                texture3 = GL20.glGetUniformLocation(program, "colorData");
            }
            glUniform1i(texture2, 1);
            glUniform1i(texture3, 2);
        }

        if (oX != Camera.getX() || oY != Camera.getY()) {
            if (cameraLocation == -1) {
                cameraLocation = GL20.glGetUniformLocation(program, "cameraPos");
            }

            glUniform2f(cameraLocation, Camera.getX(), Camera.getY());

            oX = Camera.getX();
            oY = Camera.getY();
        }

        if (!set) {
            if (resolutionLocation == -1) {
                resolutionLocation = GL20.glGetUniformLocation(program, "iResolution");
            }
            if (overallBrightnessLocation == -1) {
                overallBrightnessLocation = GL20.glGetUniformLocation(program, "overall_brightness");
            }
            if (countLocation == -1) {
                countLocation = GL20.glGetUniformLocation(program, "count");
            }

            if (windowLocation == -1) {
                windowLocation = GL20.glGetUniformLocation(program, "window");
            }

            if (aspectLocation == -1) {
                aspectLocation = GL20.glGetUniformLocation(program, "aspect");
            }

            glUniform2f(resolutionLocation, (float) GameSettings.Display.resolution.getWidth(), (float) GameSettings.Display.resolution.getHeight());
            glUniform1f(overallBrightnessLocation, overallBrightness);
            glUniform1f(countLocation, lights.size());
            glUniform2f(windowLocation, GameSettings.Display.window_width, GameSettings.Display.window_height);
            glUniform2f(aspectLocation, (float) GameSettings.Display.resolution.aspectRatio.arWidth, (float) GameSettings.Display.resolution.aspectRatio.arHeight);
            set = true;
        }

        if (!lightUpdate) {
            if (lightDataTexture == -1) {
                lightDataTexture = TextureLoader.createTextureID();

            }
            if (colorDataTexture == -1) {
                colorDataTexture = TextureLoader.createTextureID();
            }

            int format, format2;
            if (RenderService.getCapabilities().OpenGL30) {
                format = GL30.GL_RGB32F;
                format2 = GL30.GL_RGBA32F;
            } else if (RenderService.getCapabilities().GL_ARB_texture_float) {
                format = ARBTextureFloat.GL_RGB32F_ARB;
                format2 = ARBTextureFloat.GL_RGBA32F_ARB;
            } else if (RenderService.getCapabilities().GL_NV_float_buffer) {
                format = NVFloatBuffer.GL_FLOAT_RGB32_NV;
                format2 = NVFloatBuffer.GL_FLOAT_RGBA32_NV;
            } else { //We can't do lighting..abort
                RenderService.INSTANCE.runOnServiceThread(new Runnable() {

                    @Override
                    public void run() {
                        LightShader.this.setActive(false);
                    }
                }, true);
                return;
            }
            int count = 0;
            for (Light l : lights) {
                if (Camera.isOffScreen(l.getX(), l.getY(), l.getRadius(), l.getRadius()))
                    continue;
                count++;
            }
            FloatBuffer dataTexture = BufferUtils.createFloatBuffer(lights.size() * 3);
            FloatBuffer colorTexture = BufferUtils.createFloatBuffer(lights.size() * 4);
            for (Light l : lights) {
                dataTexture.put(l.getX());
                dataTexture.put(l.getY());
                dataTexture.put(l.getRadius());

                colorTexture.put(l.getColor().getRed() / 255f);
                colorTexture.put(l.getColor().getGreen() / 255f);
                colorTexture.put(l.getColor().getBlue() / 255f);
                colorTexture.put(l.getBrightness());
            }

            dataTexture.flip();
            colorTexture.flip();


            glBindTexture(GL11.GL_TEXTURE_1D, lightDataTexture);

            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage1D(GL_TEXTURE_1D, 0, format, lights.size(), 0, GL_RGB, GL_FLOAT, dataTexture);

            glBindTexture(GL_TEXTURE_1D, colorDataTexture);

            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage1D(GL_TEXTURE_1D, 0, format2, lights.size(), 0, GL_RGBA, GL_FLOAT, colorTexture);

            glBindTexture(GL_TEXTURE_1D, 0);
            lightUpdate = true;
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_1D, lightDataTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_1D, colorDataTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }
}
