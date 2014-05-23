package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.game.quests.GameQuest;

public class ControlRoom extends PhysicsSprite implements Selectable {
    @Override
    public String getSpriteName() {
        return "ControlRoom";
    }
    @Override
    public void onLoad() {
        super.onLoad();
        setAnimation("doors");
        pauseAnimation();

        setLayer(2);
    }

    @Override
    public boolean onSelected(final PlayableSprite player) {
        double angle = angleTowards(player);
        if (angle > 248.0 && angle < 268.0) {
            player.freeze();

            reverseAnimation(false);
            playAnimation();

            setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                _play(player);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    setAnimationFinishedListener(null);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public double getDistanceRequired() {
        return 65;
    }

    private void _play(PlayableSprite sprite) throws InterruptedException {
        sprite.setMovementSpeed(7);
        sprite.setLayer(1);
        sprite.setWaypoint(getX(), sprite.getY() - 48f, WaypointType.SIMPLE);
        sprite.waitForWaypointReached();
        reverseAnimation(true);
        playAnimation();

        Thread.sleep(3700);

        //TODO Play sound maybe..?
        TileObject.setTileAnimationSpeed(50L);

        reverseAnimation(false);
        playAnimation();

        Thread.sleep(1920);

        sprite.setWaypoint(getX() - 16f, sprite.getY() + 64f, WaypointType.SIMPLE);
        sprite.waitForWaypointReached();
        sprite.setLayer(2);
        sprite.unfreeze();
        GameQuest.INSTANCE.factory_beltsactive = true;

        reverseAnimation(true);
        playAnimation();
    }
}
