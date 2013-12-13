package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ImageLayer implements Drawable {
    private Texture texture;
    private String path;
    private Layer parentLayer;
    public ImageLayer(Layer parentLayer) {
        this.parentLayer = parentLayer;

        path = parentLayer.getImageLayerData();
    }

    public Layer getlayer() {
        return parentLayer;
    }

    @Override
    public float getX() {
        return parentLayer.getX();
    }

    @Override
    public float getY() {
        return parentLayer.getY();
    }

    public void init() {
        try {
            texture = Texture.retriveTexture(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        if (texture == null)
            return;
        float alpha = parentLayer.getOpacity() - (1 - RenderService.getCurrentAlphaValue());
        if (alpha < 0)
            alpha = 0;
        glColor4f(1.0f, 1.0f, 1.0f, alpha);

        texture.bind();
        float bx = texture.getTextureWidth() / 2;
        float by = texture.getTextureHeight() / 2;
        float x = getX(), y = getY();

        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        texture.unbind();

        glColor4f(1.0f, 1.0f, 1.0f, RenderService.getCurrentAlphaValue());
    }

    @Override
    public int compareTo(Drawable o) {
        return UpdatableDrawable.BEFORE;
    }
}
