package com.dissonance.game.quests;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.scenes.Demo_OpeningScene;
import com.dissonance.game.scenes.OutdoorScene;
import com.dissonance.game.sprites.menu.IntroCredits;

public class IntroSceneQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        IntroCredits intro = new IntroCredits();
        World world1 = WorldFactory.getWorld("Zesilia_pan");
        World world2 = WorldFactory.getWorld("ToZesilia");

        RenderService.INSTANCE.fadeToBlack(1); //Make screen black
        setWorld(world1);
        Sound.playSound("zesilia");
        world1.waitForWorldDisplayed();
        intro.display(world1);
        System.out.println("Play Scene");
        playSceneAndWait(Demo_OpeningScene.class);
        System.out.println("Close intro");
        intro.close();
        intro.display(world2);
        setWorld(world2);
        world2.waitForWorldDisplayed();
        intro.alwaysContinue();
        RenderService.INSTANCE.fadeToBlack(1); //Make screen black

        playSceneAndWait(OutdoorScene.class);
        setNextQuest(new GateQuest());
        endQuest();
    }

    @Override
    public String getName() {
        return "demo:level:1:quest";
    }
}
