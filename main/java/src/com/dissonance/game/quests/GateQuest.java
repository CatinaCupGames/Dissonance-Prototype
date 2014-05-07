package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.game.w.CityEntrySquare;

public class GateQuest extends AbstractQuest {
    @Override
    public void startQuest() throws Exception {
        float x = CityEntrySquare.farrand.getX();
        float y = CityEntrySquare.farrand.getY();

        do {
            Thread.sleep(10000);
            if (Math.abs(x - CityEntrySquare.farrand.getX()) >= 50 || Math.abs(y - CityEntrySquare.farrand.getY()) >= 50) break;

            Dialog.displayDialog("movement1");

            Thread.sleep(30000);
            if (Math.abs(x - CityEntrySquare.farrand.getX()) >= 50 || Math.abs(y - CityEntrySquare.farrand.getY()) >= 50) break;

            Dialog.displayDialog("movement2");

            Thread.sleep(2 * 60000);
            if (Math.abs(x - CityEntrySquare.farrand.getX()) >= 50 || Math.abs(y - CityEntrySquare.farrand.getY()) >= 50) break;

            Dialog.displayDialog("movement3");

            Thread.sleep(60000);
        } while (true);
    }

    @Override
    public String getName() {
        return "player_movement_tutorial";
    }
}
