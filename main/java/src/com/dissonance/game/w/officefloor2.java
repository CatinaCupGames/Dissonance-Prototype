package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.office.OpenWindow;

public class officefloor2 extends DemoLevelWorldLoader {
    @Override
    public void onDisplay(World w) {
        super.onDisplay(w);

        farrand.setVisible(false);
        jeremiah.setVisible(false);

        OpenWindow window = new OpenWindow();
        window.setX(56.5f * 16f);
        window.setY(3.5f * 16f);
        w.loadAndAdd(window);
    }

    @Override
    public void onRespawn(World w) {
        super.onRespawn(w);

        farrand.setUsePhysics(false);
        jeremiah.setUsePhysics(false);
        farrand.setX(18f * 16f);
        farrand.setY(8f * 16f);

        jeremiah.setX(21f * 16f);
        jeremiah.setY(8f * 16f);

        farrand.setUsePhysics(true);
        jeremiah.setUsePhysics(true);

        farrand.setVisible(false);
        jeremiah.setVisible(false);
    }
}
