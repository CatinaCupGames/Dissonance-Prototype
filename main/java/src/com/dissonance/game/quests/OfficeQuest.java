package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.sprites.animation.AbstractAnimator;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.OfficeScene;
import com.dissonance.game.w.WaldomarsMeetingRoom;

/**
 * Created by Jmerrill on 5/6/2014.
 */
public class OfficeQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("WaldomarsMeetingRoom");
        setWorld(w);
        w.waitForWorldLoaded();
        WaldomarsMeetingRoom.waldomar.setAnimation("walk_back");
        WaldomarsMeetingRoom.waldomar.pauseAnimation();
        WaldomarsMeetingRoom.waldomar.setFrame(1);
        WaldomarsMeetingRoom.farrand.setAnimation("walk_left");
        WaldomarsMeetingRoom.farrand.pauseAnimation();
        WaldomarsMeetingRoom.farrand.setFrame(1);

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(OfficeScene.class);
    }

    @Override
    public String getName() {
        return "office_quest";
    }
}
