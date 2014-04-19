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
        World w = WorldFactory.getWorld("test");
        setWorld(w, RenderService.TransitionType.FADETOBLACK);
        w.waitForWorldLoaded();

        Random rand = new Random();
        while (true) {
            if (test.farrand.getWorld() == null) {
                Thread.sleep(500);
                continue;
            }
            int num = rand.nextInt(500);
            ToastText text = test.farrand.toastText("-" + num);
            text.setTint(new Color(217,17,17));

            Thread.sleep(350);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
