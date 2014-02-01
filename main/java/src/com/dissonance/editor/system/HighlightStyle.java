package com.dissonance.editor.system;

import javax.swing.text.AttributeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class HighlightStyle {
    private static List<HighlightStyle> styles = new ArrayList<>();
    private final String name;
    private Pattern pattern;
    private final AttributeSet style;

    public HighlightStyle(String name, String pattern, AttributeSet style) {
        this.name = name;
        this.pattern = Pattern.compile(pattern);
        this.style = style;
    }

    public static synchronized void addStyle(HighlightStyle style) {
        if (!styles.contains(style)) {
            styles.add(style);
        }
    }

    public static synchronized void removeStyle(HighlightStyle style) {
        styles.remove(style);
    }

    public static HighlightStyle getStyle(String name) {
        for (HighlightStyle style : styles) {
            if (style.name.equals(name)) {
                return style;
            }
        }

        return null;
    }

    public static List<HighlightStyle> getStyles() {
        return styles;
    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public AttributeSet getStyle() {
        return style;
    }
}