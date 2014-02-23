package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import org.lwjgl.util.vector.Vector2f;

public class Demo_OpeningScene extends SimpleScene {
    @Override
    protected void playScene()throws Throwable {
        Camera.setY(30 * 16);
        RenderService.INSTANCE.fadeToBlack(1);
        RenderService.INSTANCE.waitForFade();
        Camera.linearMovement(new Vector2f(800f, Camera.getY()), 14000f);
        RenderService.INSTANCE.fadeFromBlack(10000);
        RenderService.INSTANCE.waitForFade();
        RenderService.INSTANCE.fadeToBlack(5000);
        RenderService.INSTANCE.waitForFade();

        //TODO: create indoor tileset for second scene



    }
}
