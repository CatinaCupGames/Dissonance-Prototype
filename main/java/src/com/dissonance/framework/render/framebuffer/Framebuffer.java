package com.dissonance.framework.render.framebuffer;

import com.dissonance.framework.render.Drawable;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer implements Drawable {
    private int fID, tID;

    protected int width, height;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void generate() {
        fID = glGenFramebuffers();
        tID = glGenTextures();

        glBindFramebuffer(GL_FRAMEBUFFER, fID);
        glBindTexture(GL_TEXTURE_2D, tID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (ByteBuffer) null);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, tID, 0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("Framebuffer configuration error.");
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void begin() {
        glPushMatrix();
        glBindFramebuffer(GL_FRAMEBUFFER, fID);
        glViewport(0, 0, width, height);
    }

    public void end() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glPopMatrix();
    }

    public void render() {
        glBindTexture(GL_TEXTURE_2D, tID);
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(0, 0, 0);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(width, 0, 0);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(width, height, 0);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(0, height, 0);
        glEnd();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public int compareTo(Drawable o) {
        return 0;
    }

    @Override
    public boolean neverSort() {
        return true;
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
