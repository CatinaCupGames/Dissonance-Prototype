package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.Admin;
import com.dissonance.game.sprites.RedGuard;
import com.dissonance.game.w.CityEntrySquare2;
import org.lwjgl.util.vector.Vector2f;

public class EndScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {

        CityEntrySquare2.farrand.freeze();
        CityEntrySquare2.jeremiah.freeze();

        Dialog.displayDialog("itsogre");

        CityEntrySquare2.getShrekt(getWorld());
        Vector2f pan = new Vector2f(23*16, 48*16);
        Camera.easeMovement(pan, 3000);

        Thread.sleep(1000);

        for(RedGuard gunguard : CityEntrySquare2.gunguards){
            gunguard.setAnimation("bringup_right");
        }
        for (Admin adminguard : CityEntrySquare2.adminguards){
            adminguard.setAnimation("bringup_right");
        }
    }
}
