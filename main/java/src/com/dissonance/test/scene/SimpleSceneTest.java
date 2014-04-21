package com.dissonance.test.scene;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.RenderService;

public class SimpleSceneTest extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeToBlack(1500); //Fade to black in 1500ms
        RenderService.INSTANCE.waitForFade(); //Wait for the fade event to complete
    }
}
