package com.dissonance.framework.game.world.tiled;

public enum LayerType {
    TILE_LAYER("tilelayer"),
    IMAGE_LAYER("imagelayer"),
    OBJECT_LAYER("objectgroup"),
    UNKNOWN("???");

    String type;
    LayerType(String type) { this.type = type; }

    public static LayerType parse(String type) {
        for (LayerType t : LayerType.values()) {
            if (t.type.equalsIgnoreCase(type))
                return t;
        }
        return UNKNOWN;
    }
}
