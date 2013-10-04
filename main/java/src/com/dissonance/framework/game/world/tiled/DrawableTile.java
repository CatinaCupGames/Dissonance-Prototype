package com.dissonance.framework.game.world.tiled;

import com.dissonance.framework.render.Drawable;

import static org.lwjgl.opengl.GL11.glColor4f;

public abstract class DrawableTile implements Drawable {
    protected Layer parentLayer;

    public DrawableTile(Layer parentLayer) {
        this.parentLayer = parentLayer;
    }

    public abstract float getX();

    public abstract float getY();

    public Layer getLayer() {
        return parentLayer;
    }

    @Override
    public void render() {
        glColor4f(1.0f, 1.0f, 1.0f, parentLayer.getOpacity());
    }


    @Override
    public int compareTo(Drawable o) {
        if (o instanceof DrawableTile) {
            DrawableTile t = (DrawableTile)o;
            if (t.parentLayer != null && parentLayer != null) {
                if (t.parentLayer.getLayerNumber() > parentLayer.getLayerNumber())
                    return Drawable.BEFORE;
                else if (t.parentLayer.getLayerNumber() < parentLayer.getLayerNumber())
                    return Drawable.AFTER;
                else
                    return Drawable.EQUAL;
            } else
                return Drawable.EQUAL;
        }
        return Drawable.BEFORE;
    }
}
