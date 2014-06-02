package com.dissonance.game.w.menu;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.VersionString;
import com.dissonance.game.sprites.menu.Background;
import com.dissonance.game.sprites.menu.Banner;
import com.dissonance.game.sprites.menu.buttons.CoopButton;
import com.dissonance.game.sprites.menu.buttons.ExitButton;
import com.dissonance.game.sprites.menu.buttons.PlayDemoButton;
import com.dissonance.game.sprites.menu.Static;

public class MainMenu implements WorldLoader {
    @Override
    public void onLoad(World world) {
        Background b = new Background();
        b.setX(1024f / 2f);
        b.setY(512f / 2f);
        b.setLayer(0);
        world.loadAndAdd(b);

        Banner banner = new Banner();
        banner.setX(1024f / 2f);
        banner.setY(512f / 2f);
        banner.setAlpha(227f / 255f);
        banner.setLayer(1);
        world.loadAndAdd(banner);

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(2);
        world.loadAndAdd(s);

        VersionString string = new VersionString();
        string.display(world);

        PlayDemoButton button1 = new PlayDemoButton();
        button1.display(world);

        CoopButton button2 = new CoopButton();
        button2.display(world);

        ExitButton exitButton = new ExitButton();
        exitButton.display(world);
    }

    @Override
    public void onDisplay(World world) {

    }
}
