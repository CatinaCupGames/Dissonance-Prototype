package com.dissonance.dialogcreator;

import com.dissonance.dialogcreator.ui.DialogCreator;
import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;

public final class DialogQuest extends AbstractQuest {

    @Override
    public String getName() {
        return "DialogQuest";
    }

    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("demo_opening_world");
        setWorld(world);
        world.waitForWorldLoaded();

        DialogCreator creator = new DialogCreator();
        creator.setVisible(true);
        creator.requestFocus();
        creator.toFront();
    }
}
