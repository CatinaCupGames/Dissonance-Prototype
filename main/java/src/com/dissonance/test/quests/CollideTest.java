package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.sprites.impl.ToastText;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.w.GameWorldLoader;
import com.dissonance.test.w.collideTest;
import com.dissonance.test.w.test;

import java.awt.*;
import java.util.Random;

public class CollideTest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("parallax_test");
        setWorld(w, RenderService.TransitionType.FADETOBLACK);
        w.waitForWorldLoaded();
    }

    @Override
    public String getName() {
        return null;
    }
}
