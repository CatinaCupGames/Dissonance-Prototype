package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.w.CityEntrySquare2;
import org.lwjgl.util.vector.Vector2f;

public class FinalFightScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(1500);
        Vector2f center = Camera.translateToCameraCenter(CityEntrySquare2.farrand.getVector(), CityEntrySquare2.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(CityEntrySquare2.farrand);

        RenderService.INSTANCE.fadeFromBlack(1800);
        RenderService.INSTANCE.waitForFade();

        Thread.sleep(500);

        CityEntrySquare2.jeremiah.face(Direction.RIGHT);

        Dialog.displayDialog("end scene");

        Vector2f pan = new Vector2f(76*16, 40*16);
        Camera.easeMovement(pan, 3000);
        Camera.waitForEndOfMovement();
        Camera.easeMovement(center, 1000);
        Camera.waitForEndOfMovement();
    }
}
