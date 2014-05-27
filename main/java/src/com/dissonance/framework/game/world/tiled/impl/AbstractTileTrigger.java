package com.dissonance.framework.game.world.tiled.impl;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;

public abstract class AbstractTileTrigger {
    public void onCollide(final AnimatedSprite sprite, final Tile obj) {
        try {
            onTrigger(sprite, obj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onSpriteCollide(final Sprite sprite, final Tile obj) {
        try {
            onSpriteTrigger(sprite, obj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public abstract void onTrigger(AnimatedSprite sprite, Tile tile);

    public abstract void onSpriteTrigger(Sprite sprite, Tile tile);
}
