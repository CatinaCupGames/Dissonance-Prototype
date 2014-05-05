package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.ComplexScene;
import com.dissonance.framework.game.scene.dialog.Dialog;

/**
 * Created by john on 5/5/14.
 */
public class WalktoWaldoScene extends ComplexScene{

    @Override
    protected boolean anythingToMove(Dialog lastDialog, int part) {
        return true;
    }

    @Override
    protected void moveThings(int part) throws Exception {

    }

    @Override
    protected void initScene() {

    }
}
