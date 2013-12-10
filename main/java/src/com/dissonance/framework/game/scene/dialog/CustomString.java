package com.dissonance.framework.game.scene.dialog;

import java.awt.*;
import java.util.Arrays;

public class CustomString {
    private String text;
    private Font font;
    private Style style;
    private boolean append;

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
                font = DialogUI.text_font;
                break;
            case BOLD:
                font = DialogUI.text_font.deriveFont(Font.BOLD);
                break;
            case ITALIC:
                font = DialogUI.text_font.deriveFont(Font.ITALIC);
                break;
        }
        this.append = append;
    }

    public Style getStyle() {
        return style;
    }

    public String getString() {
        return text;
    }

    public String toString() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public boolean isAppend() {
        return append;
    }
}
