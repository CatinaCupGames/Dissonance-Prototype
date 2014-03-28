package com.dissonance.framework.game.scene.dialog;

public enum Style {
    NORMAL(""),
    BOLD("bold"),
    ITALIC("italic"),
    BOLD_ITALIC("bold|italic");

    private final String id;

    Style(String id) {
        this.id = id;
    }

    /**
     * Returns the .xml identifier of the style.
     */
    public String getId() {
        return id;
    }

    public static Style forId(String id) {
        for (Style style : values()) {
            if (style.id.equalsIgnoreCase(id)) {
                return style;
            }
        }

        return Style.NORMAL;
    }
}
