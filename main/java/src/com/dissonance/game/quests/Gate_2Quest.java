package com.dissonance.game.quests;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.AtWaldomarScene;
import com.dissonance.game.w.AtWaldomar;

public class Gate_2Quest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("AtWaldomar");
        setWorld(w);
        w.waitForWorldDisplayed();

        AtWaldomar.farrand.freeze();
        AtWaldomar.jeremiah.freeze();

        RenderService.INSTANCE.fadeToAlpha(1, 0f);
        playSceneAndWait(AtWaldomarScene.class);

        setNextQuest(new HallwayQuest());
        endQuest();
    }

    @Override
    public String getName() {
        return null;
    }
}
