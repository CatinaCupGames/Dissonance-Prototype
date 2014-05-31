package com.dissonance.game.scenes;

import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.quests.BossQuest;
import com.dissonance.game.sprites.Admin;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.RedGuard;
import com.dissonance.game.w.CityEntrySquare2;
import org.lwjgl.util.vector.Vector2f;

public class EndScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        Camera.stopFollowing();
        CityEntrySquare2.farrand.freeze();
        CityEntrySquare2.jeremiah.freeze();

        CityEntrySquare2.getShrekt(getWorld());

        Direction direction = CityEntrySquare2.farrand.directionTowards(CityEntrySquare2.jeremiah);
        CityEntrySquare2.farrand.face(direction);
        CityEntrySquare2.jeremiah.face(Direction.LEFT);

        Dialog.displayDialog("itsogre");

        Sound.playSound("introtheme");

        Vector2f pan = new Vector2f(20*16, 40*16);
        Camera.easeMovement(pan, 3000);
        Camera.waitForEndOfMovement();

        Thread.sleep(1000);

        for (RedGuard guard : CityEntrySquare2.gunguards) {
            guard.face(Direction.RIGHT);
        }

        for (BlueGuard guard : CityEntrySquare2.meleeguards) {
            guard.face(Direction.RIGHT);
        }

        for (Admin admin : CityEntrySquare2.adminguards) {
            admin.face(Direction.RIGHT);
        }

        BossQuest.RAISE = true;

        for(RedGuard gunguard : CityEntrySquare2.gunguards) {
            gunguard.setAnimation("bringup_right");
            gunguard.setAnimationSpeed(400);
            gunguard.playAnimation();
        }
        for (Admin adminguard : CityEntrySquare2.adminguards) {
            adminguard.setAnimation("bringup_right");
            adminguard.setAnimationSpeed(400);
            adminguard.playAnimation();
        }

        Thread.sleep(2500);
    }
}
