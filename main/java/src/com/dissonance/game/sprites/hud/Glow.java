package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Glow extends AbstractUI {
    private static Texture texture;
    private BaseHUD hud;
    public Glow(BaseHUD hud) {
        super(hud);
        this.hud = hud;
    }


    @Override
    protected void onRender() {
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;
        float[] colors = Players.PLAYER_COLORS[hud.getPlayer().getNumber() - 1];

        glColor3f(colors[0], colors[1], colors[2]);
        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
        glColor3f(1f, 1f, 1f);
    }

    @Override
    protected void onOpen() {
        if (texture == null) {
            try {
                texture = Texture.retrieveTexture("sprites/menu/player_hud/Glow.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setWidth(texture.getTextureWidth());
        setHeight(texture.getTextureHeight());

        alignToTexture(texture);

        marginLeft(3f);
        marginBottom(24f);
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void update() {

    }
}
