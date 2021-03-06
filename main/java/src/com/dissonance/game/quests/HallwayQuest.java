package com.dissonance.game.quests;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.scenes.HallwayScene;

public class HallwayQuest  extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        Camera.stopFollowing();
        World w = WorldFactory.getWorld("WaldoHallway");
        setWorld(w);
        w.waitForWorldDisplayed();
        Sound.getSound("zesilia").fadeOut();
        Sound.fadeInSound("waldobuilding");

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(HallwayScene.class);

        setNextQuest(new OfficeQuest());
        endQuest();

    }

    @Override
    public String getName() {
        return "hallway_quest";
    }
}
