package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.behaviors.PathFollow;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.Farrand;

import javax.script.SimpleBindings;

public final class AITestQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        final SimpleBindings bindings = new SimpleBindings();
        World w = WorldFactory.getWorld("AITest");
        setWorld(w);
        w.waitForWorldLoaded();

        final Farrand farrand = new Farrand();
        farrand.setX(32);
        farrand.setY(32);
        w.loadAndAdd(farrand);
        farrand.select();
        bindings.put("farrand", farrand);

        final Farrand other = new Farrand();
        other.setX(336);
        other.setY(256);
        w.loadAndAdd(other);
        bindings.put("other", other);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if (InputKeys.isButtonPressed(InputKeys.JUMP)) {
                        other.setBehavior(new PathFollow(other, farrand.getPosition()));
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
