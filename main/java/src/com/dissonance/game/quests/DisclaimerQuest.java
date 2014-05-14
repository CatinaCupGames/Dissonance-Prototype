package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.menu.DisclaimerSprite;

public class DisclaimerQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("some_world_idk");
        setWorld(world);
        world.waitForWorldDisplayed();

        DisclaimerSprite sprite = new DisclaimerSprite();
        sprite.display(world);
        sprite.setOnCompleteEvent(new Runnable() {
            @Override
            public void run() {
                try {
                    DisclaimerQuest.super.setNextQuest(new IntroSceneQuest());
                    DisclaimerQuest.super.endQuest();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public String getName() {
        return null;
    }
}
