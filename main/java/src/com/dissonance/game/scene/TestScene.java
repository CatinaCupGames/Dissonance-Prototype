package com.dissonance.game.scene;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.ComplexScene;
import com.dissonance.framework.game.scene.Scene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.sprites.TestPlayer;

public class TestScene extends ComplexScene {
    TestPlayer moot;

    @Override
    protected boolean anythingToMove(Dialog d, int part) {
        if (d == null)
            return false;
        if (d.getId().equals("testscene-p1") && part == 0)
            return true;
        else if (d.getId().equals("testscene-p1") && part == 1)
            return false;
        else if (d.getId().equals("testscene-p2") && part == 1)
            return true;
        return false;
    }

    @Override
    protected void moveThings(int part) throws Exception {
        switch (part) {
            case 0: //After Hey wait!
                Camera.linearMovement(Camera.translateToCameraCenter(moot.getVector(), 32), 1000);
                Camera.waitForEndOfMovement();
                waitFor(0.3);
                moot.setWaypoint(new Position((int) PlayableSprite.getCurrentlyPlayingSprite().getX() - 25, (int) PlayableSprite.getCurrentlyPlayingSprite().getY()), WaypointType.SIMPLE);
                Camera.followSprite(moot);
                moot.waitForWaypointReached();
                queueDialog(DialogFactory.getDialog("testscene-p2"));
                break;
            case 1: //After Take This!
                Camera.stopFollowing();
                moot.setWaypoint(new Position((int) PlayableSprite.getCurrentlyPlayingSprite().getX() - 500, (int) PlayableSprite.getCurrentlyPlayingSprite().getY()), WaypointType.SIMPLE);
                moot.waitForWaypointReached();
                Camera.easeMovement(Camera.translateToCameraCenter(PlayableSprite.getCurrentlyPlayingSprite().getVector(), 32), 1000);
                Camera.waitForEndOfMovement();
                Camera.followPlayer();
                break;
        }
    }

    @Override
    protected void initScene() {
        moot = new TestPlayer();
        moot.setY(PlayableSprite.getCurrentlyPlayingSprite().getY());
        moot.setX(PlayableSprite.getCurrentlyPlayingSprite().getX() - 500);
        GameService.getCurrentWorld().loadAndAdd(moot);


        Dialog d = DialogFactory.getDialog("testscene-p1");
        queueDialog(d);
    }

    @Override
    protected void onEndScene() {
        GameService.getCurrentWorld().removeSprite(moot);
        super.onEndScene();
    }
}
