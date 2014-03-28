package com.dissonance.dialogcreator;

import com.dissonance.dialogcreator.ui.DialogCreator;
import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.Wyatt;

public final class DialogQuest extends AbstractQuest {

    @Override
    public String getName() {
        return "DialogQuest";
    }

    @Override
    public void startQuest() throws Exception {
        World world = WorldFactory.getWorld("testworld");
        setWorld(world);

        Wyatt wyatt = new Wyatt();
        wyatt.setX(4);
        wyatt.setY(4);
        world.loadAndAdd(wyatt);
        wyatt.select();
        wyatt.setWorld(world);

        DialogCreator creator = new DialogCreator();
        creator.setVisible(true);
        creator.requestFocus();
    }
}
