package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.WaldomarsMeetingRoom;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Jmerrill on 5/6/2014.
 */
public class OfficeScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(1500);
        Vector2f center = Camera.translateToCameraCenter(WaldomarsMeetingRoom.farrand.getVector(), WaldomarsMeetingRoom.farrand.getHeight());
        Camera.setPos(center);

    }
}
