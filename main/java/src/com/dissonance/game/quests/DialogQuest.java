package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.Main;

public class DialogQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        setWorld(WorldFactory.getWorld("main_menu_world"));
        Dialog d = DialogFactory.getDialog(Main.DID);
        DialogUI ui = new DialogUI(d);
        ui.displayUI(true, getWorld());
    }

    @Override
    public String getName() {
        return "dialog-quest";
    }
}
