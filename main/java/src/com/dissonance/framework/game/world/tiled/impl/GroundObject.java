package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import org.lwjgl.opengl.GL14;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

//TODO Actually get this working
//Because once its working, it will save alot of FPS
public class GroundObject implements Drawable {
    private int fID;
    private int tID;
    private int dID;
    private float x;
    private float y;
    private int tileWidth;
    private int w;
    private int h;
    private boolean update = true;
    private TileObject[] tiles;
    private float oAlpha;

    public GroundObject(TileObject[] tiles, int width, int height, int tileWidth) {
        fID = glGenFramebuffersEXT();
        tID = glGenTextures();
        dID = glGenRenderbuffersEXT();

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fID);

        glBindTexture(GL_TEXTURE_2D, tID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        w = width * 2;
        h = height * 2;

        this.x = calculateX(tiles);
        this.y = calculateY(tiles);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer)null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, tID, 0);

        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, dID);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, w, h);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, dID);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); //Switch back to normal buffer

        this.tiles = tiles;
        this.tileWidth = tileWidth;

        oAlpha = RenderService.getCurrentAlphaValue();
    }

    public float calculateX(TileObject[] tiles) {
        float lowestX = -999;
        for (TileObject t : tiles) {
            if (lowestX == -999 || t.getX() < lowestX) {
                lowestX = t.getX();
            }
        }
        return lowestX;
    }


    public float calculateY(TileObject[] tiles) {
        float lowestY = -999;
        for (TileObject t : tiles) {
            if (lowestY == -999 || t.getY() < lowestY) {
                lowestY = t.getX();
            }
        }
        return lowestY;
    }

    public float calculateWidth(TileObject[] tiles) {
        float lowestX = -999;
        float highestX = -999;
        for (TileObject t : tiles) {
            if (lowestX == -999 || t.getX() < lowestX) {
                lowestX = t.getX();
            }
            if (highestX == -999 || t.getX() > highestX) {
                highestX = t.getX();
            }
        }

        return highestX - lowestX;
    }

    public float calculateHeight(TileObject[] tiles) {
        float lowestY = -999;
        float highestY = -999;
        for (TileObject t : tiles) {
            if (lowestY == -999 || t.getY() < lowestY) {
                lowestY = t.getY();
            }
            if (highestY == -999 || t.getY() > highestY) {
                highestY = t.getY();
            }
        }

        return highestY - lowestY;
    }

    @Override
    public void render() {
        update = oAlpha != RenderService.getCurrentAlphaValue();
        if (update) {

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fID);
            glPushAttrib(GL_VIEWPORT_BIT);
            glPushMatrix();

            glTranslatef(100, 100, 0);
            glScalef(0.5f, 0.5f, 1f);
            for (TileObject t : tiles) {
                t.render();
            }

            update = false;
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
            glScalef(2f, 2f, 1f);
            glPopAttrib();
            glPopMatrix();
        }

        glBindTexture(GL_TEXTURE_2D, tID);
        float bx = w / 2;
        float by = h / 2;
        final float x = getX(), y = getY();

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
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return w;
    }

    @Override
    public float getHeight() {
        return h;
    }

    @Override
    public int compareTo(Drawable o) {
        return Drawable.BEFORE;
    }
}
