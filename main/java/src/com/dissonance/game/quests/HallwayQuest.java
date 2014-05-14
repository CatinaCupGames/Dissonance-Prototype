package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.scenes.HallwayScene;

public class HallwayQuest  extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("WaldoHallway");
        setWorld(w);
        w.waitForWorldDisplayed();
        Sound.playSound("waldobuilding");

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
