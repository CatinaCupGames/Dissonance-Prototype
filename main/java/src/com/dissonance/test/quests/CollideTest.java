package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.GameWorldLoader;
import com.dissonance.test.w.collideTest;

public class CollideTest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("collideTest");
        setWorld(w, RenderService.TransitionType.FADETOBLACK);
        w.waitForWorldLoaded();
        Thread.sleep(3000);
        collideTest.var1.setWaypoint(GameWorldLoader.farrand.getPosition(), WaypointType.SMART);
    }

    @Override
    public String getName() {
        return null;
    }
}
