package com.dissonance.game.scene;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.ai.Position;
import com.dissonance.framework.game.scene.Scene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.game.sprites.TestPlayer;

public class TestScene extends Scene {
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
    protected void moveThings(int part) {
        switch (part) {
            case 0: //After Hey wait!
                moot.setWaypoint(new Position((int)PlayableSprite.getCurrentlyPlayingSprite().getX() - 25, (int)PlayableSprite.getCurrentlyPlayingSprite().getY()));
                try {
                    moot.waitForWaypointReached();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queueDialog(DialogFactory.getDialog("testscene-p2"));
                break;
            case 1: //After Take This!
                moot.setWaypoint(new Position((int)PlayableSprite.getCurrentlyPlayingSprite().getX() - 300, (int)PlayableSprite.getCurrentlyPlayingSprite().getY()));
                try {
                    moot.waitForWaypointReached();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void initScene() {
        moot = new TestPlayer();
        moot.setY(PlayableSprite.getCurrentlyPlayingSprite().getY());
        moot.setX(PlayableSprite.getCurrentlyPlayingSprite().getX() - 100);
        GameService.getCurrentWorld().loadAnimatedTextureForSprite(moot);
        GameService.getCurrentWorld().addSprite(moot);


        Dialog d = DialogFactory.getDialog("testscene-p1");
        queueDialog(d);
    }

    @Override
    protected void onEndScene() {
        GameService.getCurrentWorld().removeSprite(moot);
        super.onEndScene();
    }
}
