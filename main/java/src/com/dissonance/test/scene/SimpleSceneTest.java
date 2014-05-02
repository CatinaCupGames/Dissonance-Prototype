package com.dissonance.test.scene;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.RenderService;

public class SimpleSceneTest extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        Dialog.displayDialog("road to waldos house");
    }
}
