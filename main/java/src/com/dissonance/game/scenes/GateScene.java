package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.RenderService;

public class GateScene extends SimpleScene{

    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(800);
        RenderService.INSTANCE.waitForFade();
        Dialog.displayDialog("GateScene2");

    }
}
