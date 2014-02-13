package com.dissonance.framework.render.text;

import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * A cache-able class to easily access font caches
 */
public final class FontStyle {

    private String fontName;

    private int fontSize;

    private int fontWeight;

    public FontStyle(String fontName, int fontWeight, int fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontWeight = fontWeight;
    }

    public Font getSystemFont() {
        return new Font(fontName, fontWeight, fontSize);
    }

    public Font getFileFont() throws IOException, FontFormatException {
        InputStream inputStream = ResourceLoader.getResourceAsStream(fontName);
        Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        return awtFont2.deriveFont(fontWeight, 24f);
    }



    @Override
    public boolean equals(Object o) {
        if(!(o instanceof FontStyle))
            return false;

        FontStyle b = (FontStyle)o;

        return b.fontName.equals(fontName) &&
               b.fontWeight == fontWeight &&
               b.fontSize == fontSize;
    }
}
