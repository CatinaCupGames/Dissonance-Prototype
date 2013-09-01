package com.dissonance.framework.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureRectangle;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

public class MotionBlurPostFilter
{
    public MotionBlurPostFilter(int width, int height, float alpha, float zoom)
    {
        this.width = width;
        this.height = height;
        this.alpha = alpha;
        this.zoom = zoom;
        setupTexture();
    }

    private int textureHandle;
    private int width;
    private int height;
    private float alpha;
    private float zoom;
    private int frameCount;

    public void apply()
    {
        GL11.glEnable(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT);
        GL11.glBindTexture(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, textureHandle);

        if (frameCount++ > 0)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
            GL11.glColor4f(0, 0, 0, alpha);

            viewOrtho();
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2f(width, 0);
                GL11.glVertex2f(-zoom, -zoom);
                GL11.glTexCoord2f(width, height);
                GL11.glVertex2f(-zoom, 1.0f + zoom);
                GL11.glTexCoord2f(0, height);
                GL11.glVertex2f(1.0f + zoom, 1.0f + zoom);
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(1.0f + zoom, -zoom);
            }
            GL11.glEnd();
            viewPerspective();

            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glCopyTexSubImage2D(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, 0, 0, 0, 0, 0, width, height);
        GL11.glDisable(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT);
    }

    private void setupTexture()
    {
        int[] textureHandles = new int[1];

        IntBuffer buffer = BufferUtils.createIntBuffer(textureHandles.length);
        buffer.put(textureHandles).flip();

        GL11.glGenTextures(buffer);
        textureHandle = textureHandles[0];

        int textureData[] = new int[width * height * 3];

        buffer = BufferUtils.createIntBuffer(textureData.length);
        buffer.put(textureData).flip();

        GL11.glEnable(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT);
        GL11.glBindTexture(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, textureHandle);
        GL11.glTexImage2D(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, 0, 3, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_INT, buffer);
        GL11.glTexParameteri(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glDisable(EXTTextureRectangle.GL_TEXTURE_RECTANGLE_EXT);
    }

    void viewOrtho()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(1, 0, 0, 1, -1, 1000000);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
    }

    void viewPerspective()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
    }
}
