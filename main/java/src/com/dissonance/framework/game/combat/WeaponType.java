package com.dissonance.framework.game.combat;

public enum WeaponType {
    SPELL(""),
    LIGHT_MELEE("light_melee"),
    HEAVY_MELEE("heavy_attack"),
    GUN("gun"),
    UNKNOWN("");

    String animationName;
    WeaponType(String ani) { this.animationName = ani; }

    public String getAnimationName() {
        return animationName;
    }

    public static WeaponType fromString(String nodeValue) {
        for (WeaponType w : values()) {
            if (w.name().toLowerCase().equalsIgnoreCase(nodeValue))
                return w;
        }
        return UNKNOWN;
    }
}
