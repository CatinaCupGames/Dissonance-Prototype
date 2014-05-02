package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.Demo_OpeningScene;
import com.dissonance.game.scenes.OutdoorScene;
import com.dissonance.game.sprites.menu.IntroCredits;

public class Demo_Level1_Quest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        IntroCredits intro = new IntroCredits();

        RenderService.INSTANCE.fadeToBlack(1); //Make screen black
        World w = displayWorld("Zesilia_pan", false);
        intro.display(w);

        playSceneAndWait(Demo_OpeningScene.class);
        intro.close();
        World world = displayWorld("ToZesilia", false);
        intro.display(world);
        intro.continueIt();
        playSceneAndWait(OutdoorScene.class);
    }

    @Override
    public String getName() {
        return "demo:level:1:quest";
    }
}
