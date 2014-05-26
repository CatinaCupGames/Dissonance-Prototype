package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.office.Window;
import sun.awt.windows.WInputMethod;

public class RooftopMid extends DemoLevelWorldLoader {

    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        Window window = new Window();
        window.setX(60.5f * 16f);
        window.setY(62.5f * 16f);
        window.setAnimation("breaking");
        window.setFrame(2);
        window.pauseAnimation();
        w.loadAndAdd(window);
    }
}
