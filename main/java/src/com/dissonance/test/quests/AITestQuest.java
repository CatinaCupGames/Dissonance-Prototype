package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.behaviors.Idle;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.Farrand;

import javax.script.SimpleBindings;
import java.util.Random;

public final class AITestQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        final SimpleBindings bindings = new SimpleBindings();
        World w = WorldFactory.getWorld("BlankTest");
        setWorld(w);
        w.waitForWorldLoaded();

        final Farrand farrand = new Farrand();
        farrand.setX(532);
        farrand.setY(532);
        w.loadAndAdd(farrand);
        farrand.select();
        bindings.put("farrand", farrand);

        final Farrand other = new Farrand();
        other.setX(736);
        other.setY(756);
        w.loadAndAdd(other);
        bindings.put("other", other);
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            Farrand c = new Farrand();
            other.ignoreCollisionWith(c);
            c.setX(random.nextInt(300) + 200);
            c.setY(random.nextInt(300) + 200);
            w.loadAndAdd(c);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (InputKeys.isButtonPressed(InputKeys.SELECT)) {
                        if (other.getBehavior() == null) {
                            other.setBehavior(new Idle(other, 100));
                        }
                    }
                }

            }
        }).start();
    }

    @Override
    public String getName() {
        return "dan";
    }
}
