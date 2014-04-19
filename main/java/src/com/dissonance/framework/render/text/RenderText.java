package com.dissonance.framework.render.text;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.opengl.GL11;
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
    public static final boolean ANTI_ALIAS = false;

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

    public static TrueTypeFont getFont(Font font, int fontWeight) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        FontStyle style = new FontStyle(font.getFontName(), fontWeight, font.getSize());
        if (fontCache.contains(style))
            return fontCache.get(style);

        TrueTypeFont ttf = new TrueTypeFont(font, ANTI_ALIAS);
        fontCache.put(style, ttf);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        return ttf;
    }

    public static void drawString(String text, Vector2f position) {
        TrueTypeFont ttf = getFont(GameSettings.Display.GAME_FONT, Font.PLAIN);
        drawString(ttf, text, position.x, position.y);
    }

    public static void drawString(String text, float x, float y) {
        TrueTypeFont ttf = getFont(GameSettings.Display.GAME_FONT, Font.PLAIN);
        drawString(ttf, text, x, y);
    }

    public static void drawString(String text, Vector2f position, float size) {
        Font font = GameSettings.Display.GAME_FONT.deriveFont(size);
        TrueTypeFont ttf = getFont(font, Font.PLAIN);
        drawString(ttf, text, position.x, position.y);
    }

    public static void drawString(String text, float x, float y, float size) {
        Font font = GameSettings.Display.GAME_FONT.deriveFont(size);
        TrueTypeFont ttf = getFont(font, Font.PLAIN);
        drawString(ttf, text, x, y);
    }

    public static void drawString(TrueTypeFont font, String text, Vector2f position) {
        drawString(font, text, position.x, position.y);
    }

    public static void drawString(TrueTypeFont font, String text, float x, float y) {
        //RenderService.removeScale(); //Do not scale the font up
        font.drawString((int)x, (int)y, text);
        //RenderService.resetScale(); //Reset the scale for other sprites
    }

    public static void drawString(TrueTypeFont font, String text, float x, float y, org.newdawn.slick.Color color) {
        //RenderService.removeScale(); //Do not scale the font up
        font.drawString((int)x, (int)y, text, color);
        //RenderService.resetScale(); //Reset the scale for other sprites
    }
}

