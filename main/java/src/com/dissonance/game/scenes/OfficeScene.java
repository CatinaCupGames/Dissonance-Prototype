package com.dissonance.game.scenes;

import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.scene.SimpleScene;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.spells.impl.Fire;
import com.dissonance.game.w.WaldomarsMeetingRoom;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class OfficeScene extends SimpleScene {
    @Override
    protected void playScene() throws Throwable {
        RenderService.INSTANCE.fadeFromBlack(1500);
        Vector2f center = Camera.translateToCameraCenter(WaldomarsMeetingRoom.farrand.getVector(), WaldomarsMeetingRoom.farrand.getHeight());
        Camera.setPos(center);
        Camera.followSprite(WaldomarsMeetingRoom.farrand);

        WaldomarsMeetingRoom.guard1.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard1.setWaypoint(4f * 16, 11f * 16, WaypointType.SIMPLE);
        Thread.sleep(500);
        WaldomarsMeetingRoom.guard2.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard2.setWaypoint(9f * 16, 11f * 16, WaypointType.SIMPLE);
        Thread.sleep(500);

        WaldomarsMeetingRoom.farrand.setMovementSpeed(4f);
        WaldomarsMeetingRoom.farrand.setWaypoint(6.5f*16, 8f*16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.waldomar.waitForLoaded();
        WaldomarsMeetingRoom.waldomar.face(Direction.UP);

        WaldomarsMeetingRoom.guard3.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard3.setWaypoint(14f * 16, 13f * 16, WaypointType.SIMPLE);
        Thread.sleep(500);
        WaldomarsMeetingRoom.guard4.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard4.setWaypoint(19f * 16, 13f * 16, WaypointType.SIMPLE);
        Thread.sleep(500);

        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(4f);
        WaldomarsMeetingRoom.jeremiah.setWaypoint(16f*16, 15f*16, WaypointType.SIMPLE);


        WaldomarsMeetingRoom.guard5.setMovementSpeed(4f);
        WaldomarsMeetingRoom.guard5.setWaypoint(26f * 16, 9f * 16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.farrand.waitForWaypointReached();
        WaldomarsMeetingRoom.farrand.setAnimation("walk_front");
        WaldomarsMeetingRoom.farrand.pauseAnimation();
        WaldomarsMeetingRoom.farrand.setFrame(1);

        WaldomarsMeetingRoom.guard1.waitForWaypointReached();

        WaldomarsMeetingRoom.guard1.face(Direction.UP);
        WaldomarsMeetingRoom.guard2.face(Direction.UP);
        WaldomarsMeetingRoom.farrand.face(Direction.DOWN);
        WaldomarsMeetingRoom.guard3.face(Direction.DOWN);
        WaldomarsMeetingRoom.guard4.face(Direction.DOWN);
        WaldomarsMeetingRoom.jeremiah.face(Direction.LEFT);
        WaldomarsMeetingRoom.guard5.face(Direction.LEFT);


        Dialog.displayDialog("Waldos meeting");

        Thread.sleep(500);

        WaldomarsMeetingRoom.waldomar.useSpell(new Fire(WaldomarsMeetingRoom.waldomar));

        WaldomarsMeetingRoom.farrand.setMovementSpeed(20f);
        WaldomarsMeetingRoom.farrand.setWaypoint(8.5f*16, 8f*16,WaypointType.SIMPLE);

        Thread.sleep(500);
        WaldomarsMeetingRoom.farrand.setMovementSpeed(4f);
        WaldomarsMeetingRoom.farrand.setWaypoint(6.5f*16, 6f*16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.farrand.waitForWaypointReached();
        WaldomarsMeetingRoom.farrand.face(Direction.DOWN);

        final ParticleSprite.ParticleSource source = ParticleSprite.createParticlesAt(WaldomarsMeetingRoom.waldomar.getX(), WaldomarsMeetingRoom.waldomar.getY(), 1000f, 20f, Color.WHITE, getWorld()).setTime(350);
        source.setRate(100);
        //WaldomarsMeetingRoom.fireball.setVisible(true);

        Dialog.displayDialog("waldomeeting4");


        //WaldomarsMeetingRoom.l.setX(WaldomarsMeetingRoom.waldomar.getX());
        //WaldomarsMeetingRoom.l.setY(WaldomarsMeetingRoom.waldomar.getY());

        final long START_TIME = System.currentTimeMillis();
        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                float speed = Camera.ease(20f, 50f, 1500, System.currentTimeMillis() - START_TIME);
                source.setSpeed(speed);

                long time = (long) Camera.ease(350, 200, 1500, System.currentTimeMillis() - START_TIME);
                source.setTime(time);

                float count = Camera.ease(1000f, 3000f, 1500, System.currentTimeMillis() - START_TIME);
                source.setCount(count);

                if (speed == 50f) {
                    RenderService.INSTANCE.removeServiceTick(this);
                }
            }
        }, false, true);

        WaldomarsMeetingRoom.waldomar.useSpell(new Fire(WaldomarsMeetingRoom.waldomar));

        WaldomarsMeetingRoom.farrand.setMovementSpeed(20f);
        WaldomarsMeetingRoom.farrand.setWaypoint(12.5f*16, 6f*16,WaypointType.SIMPLE);
        Thread.sleep(300);
        WaldomarsMeetingRoom.waldomar.useSpell(new Fire(WaldomarsMeetingRoom.waldomar));
        Thread.sleep(300);
        WaldomarsMeetingRoom.waldomar.useSpell(new Fire(WaldomarsMeetingRoom.waldomar));



        Thread.sleep(1500);

        Dialog.displayDialog("waldomeeting2");

        Thread.sleep(500);

        ParticleSprite.ParticleSource source1 = ParticleSprite.createParticlesAt(WaldomarsMeetingRoom.jeremiah.getX(), WaldomarsMeetingRoom.jeremiah.getY(), 50f, 20f, Color.RED, getWorld()).setTime(200);
        Camera.shake(Direction.DOWN, 4000L, 5, 0.5);
        Sound.playSound("earthquake");

        //====WINDOW====
        WaldomarsMeetingRoom.var19.setAnimation(1);
        WaldomarsMeetingRoom.var19.addAnimationFinishedListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                WaldomarsMeetingRoom.var19.setAnimation(2);
                WaldomarsMeetingRoom.var19.removeAnimationFinishedListener(this);
            }
        });
        //====WINDOW====

        WaldomarsMeetingRoom.guard1.fallOver(null);
        WaldomarsMeetingRoom.guard2.fallOver(null);
        WaldomarsMeetingRoom.guard3.fallOver(null);
        WaldomarsMeetingRoom.guard4.fallOver(null);
        WaldomarsMeetingRoom.guard5.fallOver(null);
        /*WaldomarsMeetingRoom.guard1.setMovementSpeed(30f);
        WaldomarsMeetingRoom.guard1.setWaypoint(0, 11f * 16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.guard2.setMovementSpeed(30f);
        WaldomarsMeetingRoom.guard2.setWaypoint(0, 9f*16, WaypointType.SIMPLE);

        WaldomarsMeetingRoom.guard3.setMovementSpeed(30f);
        WaldomarsMeetingRoom.guard3.setWaypoint(13*16, 17*16, WaypointType.SIMPLE);


        WaldomarsMeetingRoom.guard4.setMovementSpeed(30f);
        WaldomarsMeetingRoom.guard4.setWaypoint(21*16, 17*16, WaypointType.SIMPLE);*/


        Dialog.displayDialog("waldomeeting3");

        WaldomarsMeetingRoom.farrand.setWaypoint(WaldomarsMeetingRoom.farrand.getX(), 5*16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.farrand.setMovementSpeed(10f);
        WaldomarsMeetingRoom.farrand.waitForWaypointReached();

        WaldomarsMeetingRoom.farrand.setMovementSpeed(30f);
        Thread.sleep(300);
        WaldomarsMeetingRoom.farrand.setWaypoint(WaldomarsMeetingRoom.farrand.getX() - 64, 2f*16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.farrand.waitForWaypointReached();
        WaldomarsMeetingRoom.farrand.setVisible(false);
        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(20f);
        WaldomarsMeetingRoom.jeremiah.setWaypoint(8*16, 5*16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.jeremiah.waitForWaypointReached();
        Thread.sleep(300);
        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(30f);
        WaldomarsMeetingRoom.jeremiah.setWaypoint(8*16, 2*16, WaypointType.SIMPLE);
        WaldomarsMeetingRoom.jeremiah.waitForWaypointReached();
        WaldomarsMeetingRoom.jeremiah.setVisible(false);
    }
}
