package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.CityEntrySquare;
import org.lwjgl.util.vector.Vector2f;

public class GateScene extends SimpleScene{

    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(800);
        RenderService.INSTANCE.waitForFade();

        CityEntrySquare.guard1.setHostile(false);
        CityEntrySquare.guard2.setHostile(false);
        CityEntrySquare.guard3.setHostile(false);
        CityEntrySquare.guard4.setHostile(false);
        CityEntrySquare.guard5.setHostile(false);


        Vector2f center = Camera.translateToCameraCenter(CityEntrySquare.farrand.getVector(), CityEntrySquare.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(CityEntrySquare.farrand);
        Dialog.displayDialog("GateScene2");

    }
}
