package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.Demo_OpeningScene;
import com.dissonance.game.scenes.OutdoorScene;
import com.dissonance.game.sprites.menu.IntroCredits;

public class Demo_Level1_Quest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        IntroCredits intro = new IntroCredits();
        World world1 = WorldFactory.getWorld("Zesilia_pan");
        World world2 = WorldFactory.getWorld("ToZesilia");

        setWorld(world1);
        world1.waitForWorldLoaded();
        RenderService.INSTANCE.fadeToBlack(1); //Make screen black
        intro.display(world1);

        playSceneAndWait(Demo_OpeningScene.class);
        intro.close();
        intro.display(world2);
        setWorld(world2);
        world2.waitForWorldLoaded();
        RenderService.INSTANCE.fadeToBlack(1); //Make screen black
        intro.continueIt();
        playSceneAndWait(OutdoorScene.class);
    }

    @Override
    public String getName() {
        return "demo:level:1:quest";
    }
}
