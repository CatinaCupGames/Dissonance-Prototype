package com.dissonance.game.quests;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.game.scenes.BossStart;
import com.dissonance.game.w.CityEntrySquare;
import com.dissonance.game.w.CityEntrySquare2;

public class BossQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        Camera.stopFollowing();
        WorldFactory.clearCache();
        World world = WorldFactory.getWorld("CityEntrySquare2");
        setWorld(world);
        world.waitForWorldDisplayed();
        CityEntrySquare2.farrand.freeze(true, BossQuest.class);
        CityEntrySquare2.jeremiah.freeze(true, BossQuest.class);

        playSceneAndWait(BossStart.class);
    }

    @Override
    public String getName() {
        return "The Boss";
    }
}
