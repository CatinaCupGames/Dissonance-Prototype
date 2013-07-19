package com.tog.framework.game.world;

import com.tog.framework.game.sprites.Sprite;

import java.util.ArrayList;
import java.util.Iterator;

public class World {
    private final ArrayList<Sprite> sprites = new ArrayList<>();

    public Iterator<Sprite> getSprites() {
        return sprites.iterator();
    }
}
