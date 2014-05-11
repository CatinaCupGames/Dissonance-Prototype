package com.dissonance.framework.render.shader.impl;

import java.awt.*;

public final class Light {
    protected float x, y, radius, brightness;
    protected Color color;

    public Light(float x, float y, float size, float brightness) {
        this(x, y, size, brightness, new Color(0f, 0f, 0f));
    }

    public Light(float x, float y, float size, float brightness, Color color) {
        this.x = x;
        this.y = y;
        this.radius = size;
        this.brightness = brightness;
        this.color = color;
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


    public Color getColor() {
        return color;
    }


    public void setX(float x) {
        this.x = x;
        LightShader.lightUpdate = false;
    }

    public void setY(float y) {
        this.y = y;
        LightShader.lightUpdate = false;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        LightShader.lightUpdate = false;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        LightShader.lightUpdate = false;
    }

    public void setColor(Color color) {
        this.color = color;
        LightShader.lightUpdate = false;
    }
}
