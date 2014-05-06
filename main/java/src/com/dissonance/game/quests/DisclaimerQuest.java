package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.menu.DisclaimerSprite;

public class DisclaimerQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("some_world_idk");
        setWorld(world);
        world.waitForWorldLoaded();

        DisclaimerSprite sprite = new DisclaimerSprite();
        sprite.display(world);
        sprite.setOnCompleteEvent(new Runnable() {
            @Override
            public void run() {
                try {
                    DisclaimerQuest.super.setNextQuest(new Demo_Level1_Quest());
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
