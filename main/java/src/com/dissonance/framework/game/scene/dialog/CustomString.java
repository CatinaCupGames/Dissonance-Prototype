package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.render.text.RenderText;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

public class CustomString {
    private String text;
    private TrueTypeFont font;
    private Style style;
    private boolean append;
    private Color color = Color.WHITE;
    private long speed = 15L;

    public CustomString(String text) {
        this(text, Style.NORMAL);
    }

    public CustomString(String text, Style style) {
        this(text, style, false);
    }

    public CustomString(String text, Style style, boolean append) {
        this.text = text;
        this.style = style;
        switch (style) {
            case NORMAL:
                font = DialogUI.font;
                break;
            case BOLD:
                font = RenderText.getFont(DialogUI.original.deriveFont(Font.BOLD), Font.BOLD);
                break;
            case ITALIC:
                font = RenderText.getFont(DialogUI.original.deriveFont(Font.ITALIC), Font.ITALIC);
                break;
            case BOLD_ITALIC:
                font = RenderText.getFont(DialogUI.original.deriveFont(Font.BOLD | Font.ITALIC), Font.BOLD | Font.ITALIC);
                break;
        }
        this.append = append;
    }

    public CustomString(String text, Style style, boolean append, Color color) {
        this(text, style, append);
        this.color = color;
    }

    public CustomString(String text, Style style, boolean append, Color color, long speed) {
        this(text, style, append, color);
        this.speed = speed;
    }

    public Style getStyle() {
        return style;
    }

    public String getString() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return text;
    }

    public long getSpeed() {
        return speed;
    }

    public TrueTypeFont getFont() {
        return font;
    }

    public boolean isAppend() {
        return append;
    }
}
