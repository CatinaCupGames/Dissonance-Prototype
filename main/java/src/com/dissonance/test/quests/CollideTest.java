package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

import java.util.Random;

public class CollideTest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("parallax_test");
        setWorld(w, RenderService.TransitionType.FADETOBLACK);
        w.waitForWorldLoaded();

        Thread.sleep(3500);
        final Random random = new Random();
        while (true) {
            double d = random.nextInt(20) + 1;
            double dd = random.nextInt(4) + 1;
            System.out.println(d + " : " + dd);
            Camera.shake(Direction.LEFT, 3000, d, dd);
            Camera.waitForShakeEnd();
            Thread.sleep(1700);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
