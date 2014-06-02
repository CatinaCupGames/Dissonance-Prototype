package com.dissonance.game.sprites.menu;

import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class CenterBanner extends ImageSprite {
    public CenterBanner() {
        super("sprites/menu/CenterBanner.png");
    }

    @Override
    public void render() {
        glPushMatrix();
        glLoadIdentity();
        super.render();
        glPopMatrix();
    }
}
