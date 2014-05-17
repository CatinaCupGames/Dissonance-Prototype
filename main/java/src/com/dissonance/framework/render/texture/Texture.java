package com.dissonance.framework.render.texture;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.system.utils.Validator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    public static final int BOTTOM_LEFT = 0;
    public static final int BOTTOM_RIGHT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int TOP_LEFT = 3;

    private static double MB_USED;

    int textureId;
    private int targetId;
    private int image_width, image_height;
    private int texture_width, texture_height;
    private float width, height;
    double mb_size;
    private String resource;
    protected static final HashMap<String, Texture> cache = new HashMap<String, Texture>();

    public static double getTextureMemoryUsed() {
        return MB_USED;
    }

    public static Texture retrieveTexture(String resource) throws IOException {
        if (cache.containsKey(resource)) {
            if (Debug.isDebugging()) System.err.println("Getting texture from cache.. (" + resource + ")");
            return cache.get(resource);
        }

        Texture t = TextureLoader.getTexture(resource, GL_TEXTURE_2D, GL_RGBA8, GL_NEAREST, GL_NEAREST);
        t.resource = resource;
        MB_USED += t.mb_size;
        cache.put(resource, t);
        if (Debug.isDebugging()) System.err.println("Saving texture in cache.. (" + resource + ")");
        return t;
    }

    public static Texture convertToTexture(String name, BufferedImage image) {
        if (cache.containsKey(name))
            return cache.get(name);

        Texture t = TextureLoader.convertToTexture(image, GL_TEXTURE_2D, GL_RGBA8, GL_NEAREST, GL_NEAREST);
        t.resource = name;
        MB_USED += t.mb_size;
        cache.put(name, t);
        return t;
    }

    public static void replaceTexture(Texture replace, Texture with) {
        Validator.validateNotNull(replace, "replace");
        Validator.validateNotNull(with, "with");

        cache.put(replace.resource, with);
    }

    public static Texture getTexture(String name) {
        return cache.get(name);
    }

    public static void redrawTexture(Texture target, BufferedImage image) {
        TextureLoader.drawToTexture(image, GL_TEXTURE_2D, GL_RGBA8, GL_NEAREST, GL_NEAREST, target);
    }

    private Texture() { }

    protected Texture(Texture texture) {
        this.textureId = texture.textureId;
        this.targetId = texture.targetId;
        this.image_height = texture.image_height;
        this.image_width = texture.image_width;
        this.texture_width = texture.texture_width;
        this.texture_height = texture.texture_height;
        this.width = texture.width;
        this.height = texture.height;
    }

    public Texture(int targetId, int textureId) {
        this.targetId = targetId;
        this.textureId = textureId;
    }

    public void bind() {
        glBindTexture(targetId, textureId);
    }

    public void unbind() {
        glBindTexture(targetId, 0);
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

    public int getTextureWidth() {
        return texture_width;
    }

    public int getTextureHeight() {
        return texture_height;
    }

    public void dispose() {
        if (Debug.isDebugging()) System.err.println("Texture dispose request for " + resource);
        if (cache.containsKey(resource))
            cache.remove(resource);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                TextureLoader.disposeTexture(Texture.this);
                MB_USED -= mb_size;
            }
        });
    }
}
