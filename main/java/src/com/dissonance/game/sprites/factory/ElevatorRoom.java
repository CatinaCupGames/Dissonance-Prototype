package com.dissonance.game.sprites.factory;

import com.dissonance.game.sprites.ImageSprite;

public class ElevatorRoom extends ImageSprite {
    public ElevatorRoom() {
        super("sprites/img/Elevator_room.png");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setCutOffMargin(-80); //Never cut off
        setY(getY() + 2f); //Just move it a smidge
    }
}
