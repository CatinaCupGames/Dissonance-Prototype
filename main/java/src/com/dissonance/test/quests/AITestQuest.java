package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.LeaderFollow;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.Farrand;

import javax.script.SimpleBindings;

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

        final Farrand other2 = new Farrand();
        other2.setX(632);
        other2.setY(632);
        w.loadAndAdd(other2);
        bindings.put("other2", other);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (InputKeys.isButtonPressed(InputKeys.JUMP)) {
                        other.setBehavior(new LeaderFollow(other, farrand, new Vector(64, 64)));
                        other2.setBehavior(new LeaderFollow(other2, farrand, new Vector(-64, 64)));
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
