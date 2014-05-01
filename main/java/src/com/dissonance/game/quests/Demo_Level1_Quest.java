package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.Demo_OpeningScene;
import com.dissonance.game.scenes.OutdoorScene;

public class Demo_Level1_Quest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        loadWorldsIntoMemory("Zesilia_pan", "ToZesilia");
        RenderService.INSTANCE.fadeToBlack(1); //Make screen black
        displayWorld("Zesilia_pan", false);

        playSceneAndWait(Demo_OpeningScene.class);
        displayWorld("ToZesilia");
        playSceneAndWait(OutdoorScene.class);
    }

    @Override
    public String getName() {
        return "demo:level:1:quest";
    }
}
