package com.dissonance.game.scene;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.scene.Scene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.framework.render.Camera;
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
    protected void moveThings(int part) throws Exception {
        switch (part) {
            case 0: //After Hey wait!
                Camera.easeMovement(Camera.translateToCameraCenter(moot.getVector(), 32), 1000);
                Camera.waitForEndOfEase();
                Thread.sleep(300);
                moot.setWaypoint(new Position((int) PlayableSprite.getCurrentlyPlayingSprite().getX() - 25, (int) PlayableSprite.getCurrentlyPlayingSprite().getY()));
                moot.setSpriteMovedListener(new Sprite.SpriteEvent.SpriteMovedEvent() {
                    @Override
                    public void onSpriteMoved(Sprite sprite, float oldx, float oldy) {
                        Camera.setPos(Camera.translateToCameraCenter(sprite.getVector(), 32));
                    }
                });
                moot.waitForWaypointReached();
                moot.setSpriteMovedListener(null);
                queueDialog(DialogFactory.getDialog("testscene-p2"));
                break;
            case 1: //After Take This!
                moot.setWaypoint(new Position((int) PlayableSprite.getCurrentlyPlayingSprite().getX() - 500, (int) PlayableSprite.getCurrentlyPlayingSprite().getY()));
                moot.waitForWaypointReached();
                Camera.easeMovement(Camera.translateToCameraCenter(PlayableSprite.getCurrentlyPlayingSprite().getVector(), 32), 1000);
                Camera.waitForEndOfEase();
                break;
        }
    }

    @Override
    protected void initScene() {
        moot = new TestPlayer();
        moot.setY(PlayableSprite.getCurrentlyPlayingSprite().getY());
        moot.setX(PlayableSprite.getCurrentlyPlayingSprite().getX() - 500);
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
