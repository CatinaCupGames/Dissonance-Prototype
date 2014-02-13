package com.dissonance.framework.render.text;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Utils to easily render text with specified fonts
 */
public final class RenderText {

    // I guess we can change this later if we want to //
    private static final boolean ANTI_ALIAS = true;

    private static Hashtable<FontStyle, TrueTypeFont> fontCache;

    static {
        fontCache = new Hashtable<>();
    }



    public static TrueTypeFont getSystemFont(FontStyle style) {
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getSystemFont(String fontName) {
        FontStyle style = new FontStyle(fontName, Font.PLAIN, 12);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getSystemFont(String fontName, int fontSize) {
        FontStyle style = new FontStyle(fontName, Font.PLAIN, fontSize);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getSystemFont(String fontName, int fontSize, int fontWeight) {
        FontStyle style = new FontStyle(fontName, fontWeight, fontSize);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }



    public static TrueTypeFont getFileFont(FontStyle style) throws IOException, FontFormatException {
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getFileFont(String fontName) throws IOException, FontFormatException {
        FontStyle style = new FontStyle(fontName, Font.PLAIN, 12);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getFileFont(String fontName, int fontSize) throws IOException, FontFormatException{
        FontStyle style = new FontStyle(fontName, Font.PLAIN, fontSize);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }

    public static TrueTypeFont getFileFont(String fontName, int fontSize, int fontWeight)throws IOException, FontFormatException {
        FontStyle style = new FontStyle(fontName, fontWeight, fontSize);
        if(fontCache.containsKey(style))
            return fontCache.get(style);

        Font baseFont = style.getSystemFont();
        TrueTypeFont ttf = new TrueTypeFont(baseFont, ANTI_ALIAS);
        fontCache.put(style, ttf);
        return ttf;
    }


    public static void drawString(TrueTypeFont font, String text, Vector2f position) {
        drawString(font, text, position.x, position.y);
    }

    public static void drawString(TrueTypeFont font, String text, float x, float y) {
        font.drawString(x, y, text);
    }
}

