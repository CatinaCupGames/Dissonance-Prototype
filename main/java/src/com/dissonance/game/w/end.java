package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.menu.*;

public class end implements WorldLoader {
    @Override
    public void onLoad(World world) {
        Background b = new Background();
        b.setX(1024f / 2f);
        b.setY(512f / 2f);
        b.setLayer(0);
        world.loadAndAdd(b);

        CenterBanner banner = new CenterBanner();
        banner.setX(2048f / 2f);
        banner.setY(1024f / 2f);
        banner.setAlpha(227f / 255f);
        banner.setLayer(1);
        world.loadAndAdd(banner);

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(2);
        world.loadAndAdd(s);

        ComingSoonText text = new ComingSoonText();
        text.display(world);
    }

    @Override
    public void onDisplay(World world) {
    }
}
