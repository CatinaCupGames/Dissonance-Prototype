package com.dissonance.test.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.ai.behaviors.Flee;
import com.dissonance.framework.game.ai.behaviors.Seek;
import com.dissonance.framework.game.ai.behaviors.Wander;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.Farrand;

public final class AITestQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("AITest");
        setWorld(w);
        w.waitForWorldLoaded();

        final Farrand farrand = new Farrand();
        farrand.setX(100);
        farrand.setY(100);
        w.loadAndAdd(farrand);
        farrand.select();

        final Farrand other = new Farrand();
        other.setX(300);
        other.setY(300);
        w.loadAndAdd(other);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (InputKeys.isButtonPressed(InputKeys.JUMP)) {
                        if (other.getBehavior() == null) {
                            other.setBehavior(new Wander(other));
                        }
                    }
                }
            }
        }).start();*/

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (InputKeys.isButtonPressed(InputKeys.JUMP)) {
                        if (other.getBehavior() == null) {
                            other.setBehavior(new Flee(other, farrand, 150f));
                        }
                    }
                }
            }
        }).start();*/

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (InputKeys.isButtonPressed(InputKeys.JUMP)) {
                        if (other.getBehavior() == null) {
                            other.setBehavior(new Seek(other, farrand.getPosition()));
                        } else {
                            ((Seek) other.getBehavior()).setTarget(farrand.getPosition().vector());
                        }
                    }
                }
            }
        }).start();*/
    }

    @Override
    public String getName() {
        return "dan";
    }
}
