package com.dissonance.dialogcreator.style;

import com.dissonance.framework.game.scene.dialog.Style;
import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a range of text in a {@link javax.swing.JTextPane JTextPane} with certain styles
 * applied to it.
 */
public final class StyleRange implements Comparable<StyleRange> {
    private final int start;
    private final int end;

    private final Style style;
    private final Color color;

    public StyleRange(int start, int end, Style style, Color color) {
        if (end < start) {
            throw new IllegalArgumentException("A range cannot end before it has started.");
        }

        this.start = start;
        this.end = end;
        this.style = style;
        this.color = color;
    }

    public StyleRange(int start, int end, Style style) {
        this(start, end, style, null);
    }

    public StyleRange(int start, int end, Color color) {
        this(start, end, Style.NORMAL, color);
    }

    public StyleRange(int start, int end) {
        this(start, end, Style.NORMAL, null);
    }

    @Override
    public int compareTo(@NotNull StyleRange range) {
        return ((Integer) start).compareTo(range.start);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Style getStyle() {
        return style;
    }

    public Color getColor() {
        return color;
    }

    public String getHexColor() {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public boolean matches(StyleRange range) {
        //Don't even bother asking... IntelliJ masterrace
        return color == null && range.getColor() == null && style.equals(range.getStyle()) ||
                !(color == null || range.getColor() == null) &&
                        style.equals(range.getStyle()) && color.equals(range.getColor());

    }

    /**
     * Merges this StyleRange with another one and returns a list of
     * styles that were created in the merge.
     */
    public List<StyleRange> merge(StyleRange styleRange) {
        java.util.List<StyleRange> intersects = new ArrayList<>();

        if (styleRange.matches(this)) {
            intersects.add(styleRange.derive(Math.min(styleRange.start, start), Math.max(styleRange.end, end)));
            return intersects;
        }

        if (start > styleRange.start) {
            intersects.add(styleRange.derive(styleRange.start, start - 1));
        }

        if (start < styleRange.start) {
            intersects.add(derive(start, styleRange.start - 1));
        }

        if (end > styleRange.end) {
            intersects.add(derive(1 + styleRange.end, end));
        }

        if (end < styleRange.end) {
            intersects.add(styleRange.derive(1 + end, styleRange.end));
        }

        int start = Math.max(this.start, styleRange.start);
        int end = Math.min(this.end, styleRange.end);
        intersects.add(styleRange.blend(this).derive(start, end));

        return intersects;
    }

    /**
     * Derives a new StyleRange that has the same style and color as the
     * current one, but a different start and end positions.
     *
     * @param start The start position of the new StyleRange
     * @param end   The end position of the new StyleRange
     */
    protected StyleRange derive(int start, int end) {
        return new StyleRange(start, end, style, color);
    }

    /**
     * Blends (mixes) the style of the current StyleRange with the style
     * of the specified StyleRange.
     *
     * @param sr The StyleRange to mix with.
     */
    protected StyleRange blend(StyleRange sr) {
        Style blendStyle;
        if ((sr.style == Style.BOLD && style == Style.ITALIC) || (style == Style.BOLD && sr.style == Style.ITALIC)) {
            blendStyle = Style.BOLD_ITALIC;
        } else if (sr.style == Style.NORMAL) {
            blendStyle = style;
        } else if (sr.style == style) {
            blendStyle = Style.NORMAL;
        } else if (style == Style.BOLD && sr.style == Style.BOLD_ITALIC) {
            blendStyle = Style.ITALIC;
        } else if (style == Style.ITALIC && sr.style == Style.BOLD_ITALIC) {
            blendStyle = Style.BOLD;
        } else {
            blendStyle = sr.style;
        }

        Color blendColor = null;
        if (sr.color != null) {
            blendColor = sr.color;
        }

        if (color != null) {
            blendColor = color;
        }

        if (blendColor == null) {
            blendColor = Color.WHITE;
        }

        return new StyleRange(start, end, blendStyle, blendColor);
    }

    @Override
    public String toString() {
        return "\n[start=" + start + ", end=" + end + ", style=" + style + ", color=" + color + "]\n";
    }
}
