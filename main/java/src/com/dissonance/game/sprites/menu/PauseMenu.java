package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.Service;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class PauseMenu extends AbstractUI  {
    private Texture texture;
    private Service.ServiceRunnable runnable;

    @Override
    protected void onRender() {
        float x = getWidth() / 2f;
        float y = getHeight() / 2f;
        float bx = GameSettings.Display.game_width / 2f;
        float by = GameSettings.Display.game_height / 2f;
        float z = 0f;

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x-bx, y-by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x+bx, y-by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x+bx, y+by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x-bx, y+by, z);
        glEnd();
        texture.unbind();
    }

    @Override
    protected void onOpen() {
        //scale(false);
        if (texture == null) {
            try {
                texture = Texture.retrieveTexture("sprites/img/pause.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        runnable = RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, false, true);

        setWidth(GameSettings.Display.game_width);
        setHeight(GameSettings.Display.game_height);
    }

    @Override
    protected void onClose() {
        runnable.kill();
    }

    @Override
    public void update() {
        Players.getPlayer1().getInput().checkKeys(null);
    }

    public void reset() {

    }
}
