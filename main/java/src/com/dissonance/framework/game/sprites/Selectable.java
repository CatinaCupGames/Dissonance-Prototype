package com.dissonance.framework.game.sprites;

import com.dissonance.framework.game.player.PlayableSprite;
import org.lwjgl.util.vector.Vector2f;

public interface Selectable {
    public boolean onSelected(PlayableSprite player);

    public Vector2f getVector();

    public float getX();

    public float getY();

    public double getDistanceRequired();
}
