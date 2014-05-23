package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Switch extends ImagePhysicsSprite implements Selectable {
    public Switch(String image) { //TODO Get switch sprite or something
        super(image);
    }

    @Override
    public String hitboxConfigPath() {
        return "";
    }

    @Override
    public boolean onSelected(PlayableSprite player) {
        GameQuest.INSTANCE.turnOnBelts();
        return true;
    }

    @Override
    public double getDistanceRequired() {
        return 50;
    }
}
