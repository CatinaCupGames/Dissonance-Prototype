package com.dissonance.editor;

import com.dissonance.framework.game.ai.astar.Position;

import java.util.HashMap;

public final class Formation {
    private final String target;
    private final HashMap<String, Position> sprites;

    public Formation(String target, HashMap<String, Position> sprites) {
        this.target = target;
        this.sprites = sprites;
    }

    public Formation(String target, String[] sprites) {
        this.target = target;

        this.sprites = new HashMap<>();

        for (String sprite : sprites) {
            this.sprites.put(sprite, new Position(0, 0));
        }
    }

    public String getTarget() {
        return target;
    }

    public HashMap<String, Position> getSprites() {
        return sprites;
    }

    public void setPosition(String sprite, Position position) {
        sprites.put(sprite, position);
    }
}
