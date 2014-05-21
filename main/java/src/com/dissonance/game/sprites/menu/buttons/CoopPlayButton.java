package com.dissonance.game.sprites.menu.buttons;

import com.dissonance.framework.game.player.Players;

public class CoopPlayButton extends PlayDemoButton {

    @Override
    public void onOpen() {
        super.onOpen();

        setVisible(false);
        setActive(false);

        setX(1000);
        setY(500);
    }

    @Override
    public void update() {
        super.update();

        if (Players.getPlayersWithInput().length > 1) {
            setVisible(true);
            setActive(true);
        } else {
            setVisible(false);
            setActive(false);
        }
    }
}
