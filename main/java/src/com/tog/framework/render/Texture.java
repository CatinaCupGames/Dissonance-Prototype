package com.tog.framework.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.HashMap;

public class Texture {
    private int textureId;
    private int targetId;
    private int image_width, image_height;
    private int texture_width, texture_height;
    private float width, height;
    private float cx = -1, cy = -1;
    private static final HashMap<String, Texture> cache = new HashMap<String, Texture>();

    public static Texture retriveTexture(String resource) throws IOException {
        if (cache.containsKey(resource))
            return cache.get(resource);

        Texture t = TextureLoader.getTexture(resource, GL_TEXTURE_2D, GL_RGBA, GL_LINEAR, GL_LINEAR);
        cache.put(resource, t);
        return t;
    }

    private Texture() { }

    Texture(int targetId, int textureId) {
        this.targetId = targetId;
        this.textureId = textureId;
    }

    public void bind() {
        glBindTexture(targetId, textureId);
    }

    public void setWidth(int width) {
        this.image_width = width;
        setWidth();
    }

    public void setHeight(int height) {
        this.image_height = height;
        setHeight();
    }

    public void setTextureWidth(int tWidth) {
        this.texture_width = tWidth;
        setWidth();
    }

    public void setTextureHeight(int tHeight) {
        this.texture_height = tHeight;
        setHeight();
    }

    private void setWidth() {
        if (texture_width != 0) {
            width = ((float) image_width) / texture_width;
        }
    }

    private void setHeight() {
        if (texture_height != 0) {
            height = ((float) image_height) / texture_height;
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getImageWidth() {
        return image_width;
    }

    public int getImageHeight() {
        return image_height;
    }

    float getCx() {
        if (cx == -1)
            cx = width / 2;
        return cx;
    }

    float getCy() {
        if (cy == -1)
            cy = height / 2;
        return cy;
    }

}
