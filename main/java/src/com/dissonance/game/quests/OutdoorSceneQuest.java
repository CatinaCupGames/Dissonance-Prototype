package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.OutdoorScene;

public class OutdoorSceneQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("ToZesilia");
        setWorld(w);
        w.waitForWorldLoaded();

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(OutdoorScene.class);
    }

    @Override
    public String getName() {
        return "outdoor_scene_quest";
    }
}
