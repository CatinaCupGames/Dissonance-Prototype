package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.OfficeScene;
import com.dissonance.game.w.WaldomarsMeetingRoom;

public class OfficeQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("WaldomarsMeetingRoom");
        setWorld(w);
        w.waitForWorldLoaded();
        //Sound.playSound("waldobuilding");
        WaldomarsMeetingRoom.farrand.setAnimation("walk_left");
        WaldomarsMeetingRoom.farrand.pauseAnimation();
        WaldomarsMeetingRoom.farrand.setFrame(1);

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(OfficeScene.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(String.valueOf(WaldomarsMeetingRoom.farrand.getX()) + String.valueOf(WaldomarsMeetingRoom.farrand.getY()));
                    if(WaldomarsMeetingRoom.farrand.getX() <= 10*16 && WaldomarsMeetingRoom.farrand.getX() >= 3*16 && WaldomarsMeetingRoom.farrand.getY() <= 5*16){
                        WaldomarsMeetingRoom.farrand.deselect();
                        WaldomarsMeetingRoom.farrand.setMovementSpeed(30f);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WaldomarsMeetingRoom.farrand.setWaypoint(WaldomarsMeetingRoom.farrand.getX(), 2f*16, WaypointType.SIMPLE);
                        try {
                            WaldomarsMeetingRoom.farrand.waitForWaypointReached();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WaldomarsMeetingRoom.farrand.setVisible(false);
                        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(20f);
                        WaldomarsMeetingRoom.jeremiah.setWaypoint(8*16, 5*16, WaypointType.SIMPLE);
                        try {
                            WaldomarsMeetingRoom.jeremiah.waitForWaypointReached();
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WaldomarsMeetingRoom.jeremiah.setMovementSpeed(30f);
                        WaldomarsMeetingRoom.jeremiah.setWaypoint(8*16, 2*16, WaypointType.SIMPLE);
                        try {
                            WaldomarsMeetingRoom.jeremiah.waitForWaypointReached();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        WaldomarsMeetingRoom.jeremiah.setVisible(false);


                    }

                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public String getName() {
        return "office_quest";
    }
}
