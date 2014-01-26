package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.shader.AbstractShader;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL20.*;

public class LightShader extends AbstractShader {
    private ArrayList<Light> lights = new ArrayList<Light>();
    private float overallBrightness = 0.5f;

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
    }

    public void add(Light l) {
        this.lights.add(l);
    }

    public void clear() {
        this.lights.clear();
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
        Random random = new Random();
        int count = random.nextInt(10) + 1;
        Light l = new Light(30, 300, 0.3f, 1.3f);

        Light l2 = new Light(500, 300, 0.2f, 1.3f);

        Light l3 = new Light(199, 300, 0.9f, 2f);

        add(l);
        add(l2);
        add(l3);


        setActive(true);
    }

    private boolean set = false;
    @Override
    public void onPreRender() {
        super.onPreRender();
        final int program = getProgramID();
        int resolution = GL20.glGetUniformLocation(program, "iResolution");
        int world_brightnessVar = GL20.glGetUniformLocation(program, "overall_brightness");
        int countVar = GL20.glGetUniformLocation(program, "count");

        glUniform2f(resolution, (float) GameSettings.Display.resolution.getWidth(), (float) GameSettings.Display.resolution.getHeight());
        glUniform1f(world_brightnessVar, overallBrightness);
        glUniform1i(countVar, lights.size());

        float camerax = Camera.getX();
        float cameray = Camera.getY();
        float cx = (float)GameSettings.Display.resolution.getWidth() - camerax;
        cx /= 2f;
        float cy = (float)GameSettings.Display.resolution.getHeight() - cameray;
        cy /= 2f;
        for (int i = 0; i < lights.size(); i++) {
            Light l = lights.get(i);
            int tempLightVar = GL20.glGetUniformLocation(program, "lights[" + i + "]");
            glUniform4f(tempLightVar, ((l.x + (l.radius / 2f)) - camerax) / cx, 1.0f - (((l.y + (l.radius / 2f)) - cameray) / cy), l.brightness, l.radius);
        }
    }

    public class Light {
        private float x, y, radius, brightness;

        public Light(float x, float y, float size, float brightness) {
            this.x = x;
            this.y = y;
            this.radius = size;
            this.brightness = brightness;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getRadius() {
            return radius;
        }

        public float getBrightness() {
            return brightness;
        }


        public void setX(float x) {
            this.x = x;
            set = false;
        }

        public void setY(float y) {
            this.y = y;
            set = false;
        }

        public void setRadius(float radius) {
            this.radius = radius;
            set = false;
        }

        public void setBrightness(float brightness) {
            this.brightness = brightness;
            set = false;
        }
    }
}
