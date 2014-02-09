package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.Demo_OpeningScene;

public class Demo_Level1_Quest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        loadWorldsIntoMemory("demo_opening_world", "demo_city_level1");
        displayWorld("demo_opening_world");

        playSceneAndWait(Demo_OpeningScene.class);

        displayWorld("demo_city_level1", RenderService.TransitionType.CROSSFADE).waitForWorldLoaded();
    }

    @Override
    public String getName() {
        return "demo:level:1:quest";
    }
}
