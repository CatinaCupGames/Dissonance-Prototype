package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
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
        //Sound.playSound("waldobuilding");
        WaldomarsMeetingRoom.farrand.setAnimation("walk_left");
        WaldomarsMeetingRoom.farrand.pauseAnimation();
        WaldomarsMeetingRoom.farrand.setFrame(1);

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(OfficeScene.class);

        //new Thread(new Runnable() {
        //    @Override
        //    public void run() {
                while(true){
                    if(WaldomarsMeetingRoom.farrand.getX() <= 10 && WaldomarsMeetingRoom.farrand.getX() >= 3 && WaldomarsMeetingRoom.farrand.getY() <= 5){
                        //WaldomarsMeetingRoom.farrand.deselect();
                        WaldomarsMeetingRoom.farrand.setMovementSpeed(20f);

                    }
                }
            }
        //}).start();
    //}

    @Override
    public String getName() {
        return "office_quest";
    }
}
