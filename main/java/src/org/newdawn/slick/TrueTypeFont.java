package org.newdawn.slick;

import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.TextureLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Copyright (c) 2013, Slick2D

    All rights reserved.
    See LICENSE file for more info

    @author Slick2D
 */
public class TrueTypeFont {
    private IntObject[] charArray = new IntObject[256];
    private Map<Character, IntObject> customChars = new HashMap<Character, IntObject>();
    private boolean antiAlias;
    private int fontSize = 0;
    private int fontHeight = 0;
    private Texture fontTexture;
    private int textureWidth = 512;
    private int textureHeight = 512;
    private java.awt.Font font;
    private FontMetrics fontMetrics;

    public TrueTypeFont(java.awt.Font font, boolean antiAlias, char[] additionalChars) {
        this.font = font;
        this.fontSize = font.getSize();
        this.antiAlias = antiAlias;

        createSet(additionalChars);
    }

    public TrueTypeFont(java.awt.Font font, boolean antiAlias) {
        this(font, antiAlias, null);
    }

    protected BufferedImage getFontImage(char ch) {
        BufferedImage tempfontImage = new BufferedImage(1, 1, 2);

        Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
        if (this.antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(this.font);
        this.fontMetrics = g.getFontMetrics();
        int charwidth = this.fontMetrics.charWidth(ch);
        if (charwidth <= 0) {
            charwidth = 1;
        }
        int charheight = this.fontMetrics.getHeight();
        if (charheight <= 0) {
            charheight = this.fontSize;
        }
        BufferedImage fontImage = new BufferedImage(charwidth, charheight, 2);

        Graphics2D gt = (Graphics2D)fontImage.getGraphics();
        if (this.antiAlias) {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(this.font);

        gt.setColor(java.awt.Color.WHITE);
        int charx = 0;
        int chary = 0;
        gt.drawString(String.valueOf(ch), charx, chary + this.fontMetrics.getAscent());


        return fontImage;
    }

    private void createSet(char[] customCharsArray) {
        if ((customCharsArray != null) && (customCharsArray.length > 0)) {
            this.textureWidth *= 2;
        }
        BufferedImage imgTemp = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        Graphics2D g = (Graphics2D)imgTemp.getGraphics();

        g.setColor(new java.awt.Color(255, 255, 255, 1));
        g.fillRect(0, 0, this.textureWidth, this.textureHeight);

        int rowHeight = 0;
        int positionX = 0;
        int positionY = 0;

        int customCharsLength = customCharsArray != null ? customCharsArray.length : 0;
        for (int i = 0; i < 256 + customCharsLength; i++)
        {
            char ch;
            if (customCharsArray != null) {
                ch = i < 256 ? (char)i : customCharsArray[(i - 256)];
            } else {
                ch = (char)i;
            }

            BufferedImage fontImage = getFontImage(ch);

            IntObject newIntObject = new IntObject();

            newIntObject.width = fontImage.getWidth();
            newIntObject.height = fontImage.getHeight();
            if (positionX + newIntObject.width >= this.textureWidth)
            {
                positionX = 0;
                positionY += rowHeight;
                rowHeight = 0;
            }
            newIntObject.storedX = positionX;
            newIntObject.storedY = positionY;
            if (newIntObject.height > this.fontHeight) {
                this.fontHeight = newIntObject.height;
            }
            if (newIntObject.height > rowHeight) {
                rowHeight = newIntObject.height;
            }
            g.drawImage(fontImage, positionX, positionY, null);

            positionX += newIntObject.width;
            if (i < 256) {
                this.charArray[i] = newIntObject;
            } else {
                this.customChars.put(ch, newIntObject);
            }
            fontImage = null;
        }

        boolean oValue = TextureLoader.isFastRedraw();
        TextureLoader.setFastRedraw(true);
        this.fontTexture = Texture.convertToTexture(this.font.toString(), imgTemp);
        TextureLoader.setFastRedraw(oValue);
    }

    protected void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2) {
        float DrawWidth = drawX2 - drawX;
        float DrawHeight = drawY2 - drawY;
        float TextureSrcX = srcX / (float)this.textureWidth;
        float TextureSrcY = srcY / (float)this.textureHeight;
        float SrcWidth = srcX2 - srcX;
        float SrcHeight = srcY2 - srcY;
        float RenderWidth = SrcWidth / (float)this.textureWidth;
        float RenderHeight = SrcHeight / (float)this.textureHeight;

        glTexCoord2f(TextureSrcX, TextureSrcY);
        glVertex2f(drawX, drawY);
        glTexCoord2f(TextureSrcX, TextureSrcY + RenderHeight);
        glVertex2f(drawX, drawY + DrawHeight);
        glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight);
        glVertex2f(drawX + DrawWidth, drawY + DrawHeight);
        glTexCoord2f(TextureSrcX + RenderWidth, TextureSrcY);
        glVertex2f(drawX + DrawWidth, drawY);
    }

    public int getWidth(String whatchars) {
        int totalwidth = 0;
        IntObject intObject = null;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); i++)
        {
            currentChar = whatchars.charAt(i);
            if (currentChar < 256) {
                intObject = this.charArray[currentChar];
            } else {
                intObject = (IntObject)this.customChars.get((char) currentChar);
            }
            if (intObject != null) {
                totalwidth += intObject.width;
            }
        }
        return totalwidth;
    }

    public int getHeight() {
        return this.fontHeight;
    }

    public int getHeight(String HeightString) {
        return this.fontHeight;
    }

    public int getLineHeight() {
        return this.fontHeight;
    }

    public void drawString(float x, float y, String whatchars, Color color) {
        drawString(x, y, whatchars, color, 0, whatchars.length() - 1);
    }

    public void drawString(float x, float y, String whatchars, Color color, int startIndex, int endIndex) {
        color.bind();
        this.fontTexture.bind();

        IntObject intObject = null;


        glBegin(GL_QUADS);
        int totalwidth = 0;
        for (int i = 0; i < whatchars.length(); i++)
        {
            int charCurrent = whatchars.charAt(i);
            if (charCurrent < 256) {
                intObject = this.charArray[charCurrent];
            } else {
                intObject = this.customChars.get((char) charCurrent);
            }
            if (intObject != null)
            {
                if ((i >= startIndex) || (i <= endIndex)) {
                    drawQuad(x + totalwidth, y, x + totalwidth + intObject.width, y + intObject.height, intObject.storedX, intObject.storedY, intObject.storedX + intObject.width, intObject.storedY + intObject.height);
                }
                totalwidth += intObject.width;
            }
        }
        glEnd();

        color.unbind();
    }

    public void drawString(float x, float y, String whatchars) {
        drawString(x, y, whatchars, Color.white);
    }

    protected class IntObject {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        private IntObject() {}
    }
}
