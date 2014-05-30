package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.scenes.ElevatorScene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ElevatorDoor extends AnimatedSprite implements Selectable, Collidable {
    private HitBox[] hb;
    @Override
    public String getSpriteName() {
        return "elevator_door";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setX(getX() + 4.5f);
        setY(getY() + 6.5f);

        float width = getWidth(), height = getHeight();
        if (getTexture() != null) {
            width = getTexture().getImageWidth();
            height = getTexture().getImageHeight();
        }
        hb = PhysicsSprite.readHitboxConfig("sprites/" + getSpriteName() + "/hitbox.txt", width, height);

        HitBox.registerSprite(this);
    }

    @Override
    public void onUnload() {
        HitBox.unregisterSprite(this);
    }

    @Override
    public HitBox getHitBox() {
        return hb[0];
    }

    private float heightC = -1;
    private float widthC = -1;
    @Override
    public boolean isPointInside(float x, float y) {
        if (!visible)
            return false;
        if (heightC == -1 || widthC == -1) {
            if (getTexture() instanceof SpriteTexture) {
                SpriteTexture temp = (SpriteTexture) getTexture();
                heightC = temp.getHeight();
                widthC = temp.getWidth();
            } else {
                heightC = getHeight();
                widthC = getWidth();
            }
        }

        float sX = getX() - (widthC / 2f);
        float sY = getY() - (heightC / 2f);
        for (HitBox hitBox : hb) {
            float minX = sX + hitBox.getMinX(), minY = sY + hitBox.getMinY(), maxX = sX + hitBox.getMaxX(), maxY = sY + hitBox.getMaxY();
            if (x > minX && y > minY && x <= maxX && y <= maxY)
                return true;
        }
        return false;
    }

    private static boolean active = false;
    @Override
    public boolean onSelected(final PlayableSprite player) {
        if (active) return false;
        double angle = angleTowards(player);
        if (angle > 227.0 && angle < 314.0) {
            if (player.getWorld().getName().equals("officefloor2")) {
                active = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog.displayDialog("haxfix");
                        active = false;
                    }
                }).start();
                return true;
            }
            player.freeze();
            player.ignoreCollisionWith(this);

            setAnimation("opening");
            reverseAnimation(false);
            playAnimation();
            active = true;
            addAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    removeAnimationFinishedListener(this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            player.setUsePhysics(false);

                            Player player1 = Players.getPlayer1();
                            Player player2 = Players.getPlayer(2);
                            if (player1.getSprite() == player) {
                                if (player2 != null && player2.getSprite() != null) {
                                    player2.getSprite().freeze();
                                    player2.getSprite().disappear();
                                }
                            } else if (player2 != null && player2.getSprite() != null && player2.getSprite().equals(player)) {
                                player1.getSprite().freeze();
                                player1.getSprite().disappear();
                            }
                            int ol = getLayer();
                            player.setLayer(1);
                            player.setMovementSpeed(8f);
                            if (player.getWorld().getName().startsWith("Factory"))
                                player.setWaypoint(29f * 16f, 7f * 16f, WaypointType.SIMPLE);
                            else {
                                setLayer(2);
                                if (player.getWorld().getName().endsWith("2"))
                                    player.setWaypoint(17f * 16f, 4f * 16f, WaypointType.SIMPLE);
                                else
                                    player.setWaypoint(47f * 16f, 4f * 16f, WaypointType.SIMPLE);
                            }
                            try {
                                player.waitForWaypointReached();
                                Thread.sleep(1200);

                                player.face(Direction.DOWN);

                                reverseAnimation(true);
                                playAnimation();
                                waitForAnimationEnd();

                                RenderService.INSTANCE.fadeToBlack(1300);
                                RenderService.INSTANCE.waitForFade();

                                player.setUsePhysics(true);

                                setLayer(ol);

                                if (player.getWorld().getName().startsWith("Factory"))
                                    GameQuest.INSTANCE.changeToRooftopMid();
                                else if (player.getWorld().getName().equals("OfficeFloor1"))
                                    GameQuest.INSTANCE.changeToOffice2();
                                else
                                    GameQuest.INSTANCE.backToOffice1();
                                active = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public double getDistanceRequired() {
        return 68;
    }
}
