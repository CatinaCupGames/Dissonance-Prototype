package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.shader.AbstractShader;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL20.*;

public class LightShader extends AbstractShader {
    private ArrayList<Light> lights = new ArrayList<Light>();
    private float overallBrightness = 0.5f;
    private int resolutionLocation = -1, overallBrightnessLocation = -1, countLocation = -1, windowLocation = -1, cameraLocation = -1, aspectLocation = -1;
    private float oX, oY;

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

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void add(Light l) {
        this.lights.add(l);
        set = false;

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void clear() {
        this.lights.clear();

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void remove(Light l) {
        this.lights.remove(l);

        if (lights.size() > 0)
            setActive(true);
        else
            setActive(false);
    }

    public void remove(int index) {
        this.lights.remove(index);

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
    @Override
    public void onPreRender() {
        super.onPreRender();
        final int program = getProgramID();
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

            glUniform2f(resolutionLocation, (float)GameSettings.Display.resolution.getWidth(), (float)GameSettings.Display.resolution.getHeight());
            glUniform1f(overallBrightnessLocation, overallBrightness);
            glUniform1i(countLocation, lights.size());
            glUniform2f(windowLocation, GameSettings.Display.window_width, GameSettings.Display.window_height);
            glUniform2f(aspectLocation, (float)GameSettings.Display.resolution.aspectRatio.arWidth, (float)GameSettings.Display.resolution.aspectRatio.arHeight);

            for (int i = 0; i < lights.size(); i++) {
                Light l = lights.get(i);
                int tempLightVar = GL20.glGetUniformLocation(program, "lights[" + i + "]");
                glUniform4f(tempLightVar, l.x, l.y, l.brightness, l.radius);

                int tempColorVar = GL20.glGetUniformLocation(program, "colors[" + i + "]");
                glUniform3f(tempColorVar, l.color.getRed() / 255f, l.color.getGreen() / 255f, l.color.getBlue() / 255f);
            }
            set = true;
        }
    }
}
